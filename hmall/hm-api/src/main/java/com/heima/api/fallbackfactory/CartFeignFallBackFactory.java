package com.heima.api.fallbackfactory;

import com.heima.api.client.CartOpenFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;

@Slf4j
public class CartFeignFallBackFactory implements FallbackFactory<CartOpenFeignClient> {
    @Override
    public CartOpenFeignClient create(Throwable cause) {
        return new CartOpenFeignClient() {
            @Override
            public void deleteCartItemByIds(Collection<Long> ids) {
                log.info("删除购物车商品失败");
            }
        };
    }
}
