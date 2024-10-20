package com.itheima.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Slf4j
class PublisherApplicationTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testWorkQueue() {
        // 队列名称
        String queueName = "work.queue";
        // 消息
        String message = "hello, spring amqp, i'm work ";
        for (int i = 0; i < 50; i++) {
            rabbitTemplate.convertAndSend(queueName, message + i);
        }
    }

    @Test
    public void testFanOutQueue() {
        // 队列名称
        String queueName = "hmall.fanout";
        // 消息
        String message = "hello, fanout exchange, i'm work ";

        rabbitTemplate.convertAndSend(queueName, null, message);

    }


    @Test
    public void testDirectQueue() throws InterruptedException {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("send message", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if(result.isAck()){
                    log.debug("消息发送成功， 收到ack");
                }else{
                    log.error("消息发送失败，收到nack, reason : {}", result.getReason());
                }
            }
        });
        // 队列名称
        String queueName = "hmall.direct";
        // 消息
        String message = "hello, direct exchange, i'm work ";
        rabbitTemplate.convertAndSend(queueName, "pink", message, correlationData);
        Thread.sleep(2000);
    }

    @Test
    public void testTopicQueue() {
        // 队列名称
        String queueName = "hmall.topic";
        // 消息
        String message = "hello, direct exchange, i'm work topic";

        rabbitTemplate.convertAndSend(queueName, "china.news", message);
    }

    @Test
    public void testDirectNoteQueue() {
        // 队列名称
        String queueName = "hmall.direct_note";
        // 消息
        String message = "hello, direct exchange, i'm work topic";

        rabbitTemplate.convertAndSend(queueName, null, message);
    }

    @Test
    public void testSendObject() {
        // 队列名称
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "jack");
        map.put("password", 123);
        rabbitTemplate.convertAndSend("object.queue", map);
    }
}