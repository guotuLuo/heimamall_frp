package com.heima.api.client;


import com.heima.api.config.OpenFeignLoggerLevelConfiguration;
import com.heima.api.domain.dto.ItemDTO;
import com.heima.api.domain.dto.OrderDetailDTO;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
// 在全局生效日志级别
//@EnableFeignClients(defaultConfiguration = OpenFeignLoggerLevelConfiguration.class)

// 在当前feign端生效日志级别
@FeignClient(value = "item-service", configuration = OpenFeignLoggerLevelConfiguration.class)
public interface ItemOpenFeignClient {
    @GetMapping("/items")
    List<ItemDTO> queryItemByIds(@RequestParam Collection<Long> ids);
    @PutMapping("/items/stock/deduct")
    void deductStock(@RequestBody List<OrderDetailDTO> items);
}
