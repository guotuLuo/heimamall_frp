package com.heima.api.config;

import com.hmall.common.utils.UserContext;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;

public class OpenFeignLoggerLevelConfiguration {
    @Bean
    public Logger.Level openFeginLoggerLevel(){
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate requestTemplate) {
                Long user = UserContext.getUser();
                if(user != null){
                    requestTemplate.header("userInfo", user.toString());
                }
            }
        };
    }

}
