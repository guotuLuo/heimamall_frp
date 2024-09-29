package com.heima.api.fallbackfactory;

import com.heima.api.client.UserOpenFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;

@Slf4j
public class UserFeignFallBackFactory implements FallbackFactory<UserOpenFeignClient> {
    @Override
    public UserOpenFeignClient create(Throwable cause) {
        return new UserOpenFeignClient() {
            @Override
            public void deductMoney(String pw, Integer amount) {
                log.info("扣除余额失败");
            }
        };
    }
}
