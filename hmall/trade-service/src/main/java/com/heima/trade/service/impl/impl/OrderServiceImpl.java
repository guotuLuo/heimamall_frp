package com.heima.trade.service.impl.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.api.client.CartOpenFeignClient;
import com.heima.api.client.ItemOpenFeignClient;
import com.hmall.common.exception.BadRequestException;
import com.hmall.common.utils.UserContext;
import com.heima.api.domain.dto.ItemDTO;
import com.heima.api.domain.dto.OrderDetailDTO;
import com.heima.api.domain.dto.OrderFormDTO;
import com.heima.api.domain.po.Order;
import com.heima.api.domain.po.OrderDetail;
import com.heima.trade.mapper.OrderMapper;

import com.heima.trade.service.IOrderDetailService;
import com.heima.trade.service.IOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final ItemOpenFeignClient itemOpenFeignClient;
    private final IOrderDetailService detailService;
    private final CartOpenFeignClient cartOpenFeignClient;
    @Override
//    @Transactional
    @GlobalTransactional
    public Long createOrder(OrderFormDTO orderFormDTO) {
        log.info("Received order request ds65a56sd56as5d65a55d56565d1a2s1d21as4d54a: " + orderFormDTO);
        // 1.订单数据
        Order order = new Order();
        // 1.1.查询商品
        List<OrderDetailDTO> detailDTOS = orderFormDTO.getDetails();
        // 1.2.获取商品id和数量的Map
        Map<Long, Integer> itemNumMap = detailDTOS.stream()
                .collect(Collectors.toMap(OrderDetailDTO::getItemId, OrderDetailDTO::getNum));
        Set<Long> itemIds = itemNumMap.keySet();
        // 1.3.查询商品
        List<ItemDTO> items = itemOpenFeignClient.queryItemByIds(itemIds);
        if (items == null || items.size() < itemIds.size()) {
            throw new BadRequestException("商品不存在");
        }
        // 1.4.基于商品价格、购买数量计算商品总价：totalFee
        int total = 0;
        for (ItemDTO item : items) {
            total += item.getPrice() * itemNumMap.get(item.getId());
        }
        order.setTotalFee(total);
        // 1.5.其它属性
        order.setPaymentType(orderFormDTO.getPaymentType());
        order.setUserId(UserContext.getUser());
        order.setStatus(1);
        // 1.6.将Order写入数据库order表中
        save(order);

        // 2.保存订单详情
        List<OrderDetail> details = buildDetails(order.getId(), items, itemNumMap);
        detailService.saveBatch(details);

        // 3.清理购物车商品
        // 这里会调用购物车服务，清除购物车内商品
        // 但是微服务内部的服务调用并不经过网关，不会携带上uerinfo请求头，也就不会通过cartservice查找mysql删除购物车商品信息
        // 总的来说，微服务之间的服务调用，也需要通过一个拦截器来放行或者达到添加请求头的效果，
        // 考虑到这个和各个微服务之前的拦截器很想，openfeign提供了这样的拦截器

        cartOpenFeignClient.deleteCartItemByIds(itemIds);

        // 4.扣减库存
        try {
            itemOpenFeignClient.deductStock(detailDTOS);
        } catch (Exception e) {
            throw new RuntimeException("库存不足！");
        }
        log.info("订单id{}", order.getId());
        return order.getId();
    }



    @Override
    public void markOrderPaySuccess(Long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(2);
        order.setPayTime(LocalDateTime.now());
        updateById(order);
    }

    private List<OrderDetail> buildDetails(Long orderId, List<ItemDTO> items, Map<Long, Integer> numMap) {
        List<OrderDetail> details = new ArrayList<>(items.size());
        for (ItemDTO item : items) {
            OrderDetail detail = new OrderDetail();
            detail.setName(item.getName());
            detail.setSpec(item.getSpec());
            detail.setPrice(item.getPrice());
            detail.setNum(numMap.get(item.getId()));
            detail.setItemId(item.getId());
            detail.setImage(item.getImage());
            detail.setOrderId(orderId);
            details.add(detail);
        }
        return details;
    }

}
