package com.itheima.publisher.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqConfiguration {
    private final RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.error("触发return callback,");
            log.debug("exchange: {}", returnedMessage.getExchange());
            log.debug("routingKey: {}", returnedMessage.getRoutingKey());
            log.debug("message: {}", returnedMessage.getMessage());
            log.debug("replyCode: {}", returnedMessage.getReplyCode());
            log.debug("replyText: {}", returnedMessage.getReplyText());
        });
    }
}
