package com.itheima.consumer.listener;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringRabbitListener {
//    @RabbitListener(queues = "simple.queue")
//    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
//        System.out.println("spring 消费者接收到消息：【" + msg + "】");
//    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueueMessage1(String msg) throws InterruptedException {
        System.out.println("i'm 1");
        System.out.println("spring 消费者接收到消息：【" + msg + "】");
        Thread.sleep(25);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueueMessage2(String msg) throws InterruptedException {
        System.out.println("i'm 2");
        System.err.println("spring 消费者接收到消息：【" + msg + "】");
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout1.queue")
    public void listenFanOutQueueMessage1(String msg) throws InterruptedException {
        System.out.println("i'm 1");
        System.out.println("spring fanout 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(25);
    }

    @RabbitListener(queues = "fanout2.queue")
    public void listenFanOutQueueMessage2(String msg) throws InterruptedException {
        System.out.println("i'm 2");
        System.err.println("spring fanout 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(200);
    }

    @RabbitListener(queues = "direct1.queue")
    public void listenDirectQueueMessage1(String msg) throws InterruptedException {
        System.out.println("spring direct 1 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(25);
    }

    @RabbitListener(queues = "direct2.queue")
    public void listenDirectQueueMessage2(String msg) throws InterruptedException {
        System.err.println("spring direct 2 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(200);
    }

    @RabbitListener(queues = "topic1.queue")
    public void listenTopicQueueMessage1(String msg) throws InterruptedException {
        System.out.println("spring direct 1 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(25);
    }

    @RabbitListener(queues = "topic2.queue")
    public void listenTopicQueueMessage2(String msg) throws InterruptedException {
        System.err.println("spring direct 2 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(200);
    }

    @RabbitListener(queues = "topic3.queue")
    public void listenTopicQueueMessage3(String msg) throws InterruptedException {
        System.err.println("spring direct 3 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(200);
    }

    @RabbitListener(queues = "topic4.queue")
    public void listenTopicQueueMessage4(String msg) throws InterruptedException {
        System.err.println("spring direct 4 消费者接收到消息：【" + msg + "】");
//        Thread.sleep(200);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct_note", durable = "true"),
            exchange = @Exchange(name = "hmall.direct_note", type = ExchangeTypes.DIRECT),
            key = {""}
    ))
    public void listenDirectNoteQueueMessage(String msg) throws InterruptedException{
        System.err.println("spring direct note 消费者接收消息:" + msg);
    }


    @RabbitListener(queues = "object.queue")
    public void listenObjectQueueMessage(Map<String, Object> msg) throws InterruptedException {
        System.err.println("spring object  消费者接收到消息：【" + msg + "】");
//        Thread.sleep(200);
    }
}
