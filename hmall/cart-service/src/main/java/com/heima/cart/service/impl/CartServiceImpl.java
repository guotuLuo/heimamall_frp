package com.heima.cart.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.api.client.ItemOpenFeignClient;
import com.heima.api.domain.dto.ItemDTO;
import com.heima.cart.config.CartProperties;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.common.utils.CollUtils;
import com.hmall.common.utils.UserContext;
import com.heima.cart.domain.dto.CartFormDTO;
import com.heima.cart.domain.po.Cart;
import com.heima.cart.domain.vo.CartVO;
import com.heima.cart.mapper.CartMapper;
import com.heima.cart.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * <p>
 * 订单详情表 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
@Service
@RequiredArgsConstructor

public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {
//    public final RestTemplate restTemplate;

//    public final DiscoveryClient discoveryClient;

    public final CartProperties cartProperties;

    public final ItemOpenFeignClient itemOpenFeignClient;
//    @Autowired
//    private final IItemService itemService;

    @Override
    public void addItem2Cart(CartFormDTO cartFormDTO) {
        // 1.获取登录用户
        Long userId = UserContext.getUser();

        // 2.判断是否已经存在
        if(checkItemExists(cartFormDTO.getItemId(), userId)){
            // 2.1.存在，则更新数量
            baseMapper.updateNum(cartFormDTO.getItemId(), userId);
            return;
        }
        // 2.2.不存在，判断是否超过购物车数量
        checkCartsFull(userId);

        // 3.新增购物车条目
        // 3.1.转换PO
        Cart cart = BeanUtils.copyBean(cartFormDTO, Cart.class);
        // 3.2.保存当前用户
        cart.setUserId(userId);
        // 3.3.保存到数据库
        save(cart);
    }

    @Override
    public List<CartVO> queryMyCarts() {
        // 1.查询我的购物车列表
        // 为什么查询为空
        System.out.println(UserContext.getUser());
        List<Cart> carts = lambdaQuery().eq(Cart::getUserId, UserContext.getUser()).list();
        if (CollUtils.isEmpty(carts)) {
            return CollUtils.emptyList();
        }

        // 2.转换VO
        List<CartVO> vos = BeanUtils.copyList(carts, CartVO.class);

        // 3.处理VO中的商品信息
        handleCartItems(vos);

        // 4.返回
        return vos;
    }


    // 这个需要改一下，这里删除购物车里的货物不生效
    @Override
    public void removeByItemId(Long itemId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
        queryWrapper.lambda()
                .eq(Cart::getUserId, UserContext.getUser())
                .eq(Cart::getItemId, itemId);
        // 2.删除
        remove(queryWrapper);
    }

    private void handleCartItems(List<CartVO> vos) {
        // 睡500ms
        // 那么每个线程qps低于2
        // TODO 1.获取商品id
        Set<Long> itemIds = vos.stream().map(CartVO::getItemId).collect(Collectors.toSet());
//        // 2.查询商品
//        List<ItemDTO> items = itemService.queryItemByIds(itemIds);
//        if (CollUtils.isEmpty(items)) {
//            return;
//        }
//        // 3.转为 id 到 item的map
//        Map<Long, ItemDTO> itemMap = items.stream().collect(Collectors.toMap(ItemDTO::getId, Function.identity()));
//        // 4.写入vo
//        for (CartVO v : vos) {
//            ItemDTO item = itemMap.get(v.getItemId());
//            if (item == null) {
//                continue;
//            }
//            v.setNewPrice(item.getPrice());
//            v.setStatus(item.getStatus());
//            v.setStock(item.getStock());
//        }

//        // TODO 1 利用RestTemplte 查询商品id
//        Set<Long> itemsId = vos.stream().map(CartVO::getItemId).collect(Collectors.toSet());
//
//        // 利用DiscoveryClient 获得所有item-service的服务列表
//        List<ServiceInstance> instances = discoveryClient.getInstances("item-service");
//        if(CollUtils.isEmpty(instances)){
//            return;
//        }
//        ServiceInstance serviceInstance = instances.get(RandomUtil.randomInt(instances.size()));
//        // TODO 2 利用从购物车数据库中获取到的商品名称，从其他服务端获取商品
//        ResponseEntity<List<ItemDTO>> response = restTemplate.exchange(
//                // 这种写法写死了url,
//                // "https://locahost:8081/item/items/ids={ids}",
//                serviceInstance.getUri() + "/items?ids={ids}",
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<ItemDTO>>() {
//                },
//                Map.of("ids", CollUtils.join(itemsId, ","))
//        );
//
//        if(!response.getStatusCode().is2xxSuccessful()){
//            return;
//        }
//        List<ItemDTO> body = response.getBody();


        // 这里调用了item服务，有可能由于购物车访问服务宕机，造成item访问服务也宕机
        List<ItemDTO> body = itemOpenFeignClient.queryItemByIds(itemIds);

        if (CollUtils.isEmpty(body)) {
            return;
        }
        // 3.转为 id 到 item的map
        Map<Long, ItemDTO> itemMap = body.stream().collect(Collectors.toMap(ItemDTO::getId, Function.identity()));
        // 4.写入vo
        for (CartVO v : vos) {
            ItemDTO item = itemMap.get(v.getItemId());
            if (item == null) {
                continue;
            }
            v.setNewPrice(item.getPrice());
            v.setStatus(item.getStatus());
            v.setStock(item.getStock());
        }
    }

    @Override
    public void removeByItemIds(Collection<Long> itemIds) {
        // 1.构建删除条件，userId和itemId
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
        queryWrapper.lambda()
                .eq(Cart::getUserId, UserContext.getUser())
                .in(Cart::getItemId, itemIds);
        // 2.删除
        remove(queryWrapper);
    }

    private void checkCartsFull(Long userId) {
        int count = Math.toIntExact(lambdaQuery().eq(Cart::getUserId, userId).count());
        if (count >= cartProperties.getMaxAmount()) {
            throw new BizIllegalException(StrUtil.format("用户购物车课程不能超过{}", cartProperties.getMaxAmount()));
        }
    }

    private boolean checkItemExists(Long itemId, Long userId) {
        int count = Math.toIntExact(lambdaQuery()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getItemId, itemId)
                .count());
        return count > 0;
    }
}
