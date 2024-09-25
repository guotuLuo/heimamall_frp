package com.heima.api.client;

import com.heima.api.config.OpenFeignLoggerLevelConfiguration;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient(value = "user-service", configuration = OpenFeignLoggerLevelConfiguration.class)
public interface UserOpenFeignClient {
    @PutMapping("/users/money/deduct")
    public void deductMoney(@RequestParam("pw") String pw,@RequestParam("amount") Integer amount);
}
