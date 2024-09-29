package com.heima.api.client;

import com.heima.api.config.OpenFeignLoggerLevelConfiguration;
import com.heima.api.fallbackfactory.CartFeignFallBackFactory;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(value = "cart-service", configuration = OpenFeignLoggerLevelConfiguration.class, fallbackFactory = CartFeignFallBackFactory.class)
public interface CartOpenFeignClient {
    @DeleteMapping("/carts")
    void deleteCartItemByIds(@RequestParam("ids") Collection<Long> ids);
}
