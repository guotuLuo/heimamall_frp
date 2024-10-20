package com.heima.api.client;

import com.heima.api.config.OpenFeignLoggerLevelConfiguration;
import com.heima.api.fallbackfactory.TradeFeignFallBackFactory;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(value = "trade-service", configuration = OpenFeignLoggerLevelConfiguration.class, fallbackFactory = TradeFeignFallBackFactory.class)
public interface TradeOpenFeignClient {

    @PutMapping("/orders/{orderId}")
    void markOrderPaySuccess(@PathVariable("orderId") Long orderId);
}
