package com.heima.api.fallbackfactory;

import com.heima.api.client.TradeOpenFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class TradeFeignFallBackFactory implements FallbackFactory<TradeOpenFeignClient> {
    @Override
    public TradeOpenFeignClient create(Throwable cause) {
        return new TradeOpenFeignClient() {
            @Override
            public void markOrderPaySuccess(Long orderId) {
                log.info("订单支付失败");
            }
        };
    }
}
