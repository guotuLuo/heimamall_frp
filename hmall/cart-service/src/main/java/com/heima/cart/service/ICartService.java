package com.heima.cart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.cart.domain.dto.CartFormDTO;
import com.heima.cart.domain.po.Cart;
import com.heima.cart.domain.vo.CartVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 订单详情表 服务类
 * </p>
 *
 * @author 虎哥
 * @since 2023-05-05
 */
public interface ICartService extends IService<Cart> {

    void addItem2Cart(CartFormDTO cartFormDTO) throws Exception;

    List<CartVO> queryMyCarts();

    void removeByItemId(Long itemId);
    void removeByItemIds(Collection<Long> itemIds);
}
