package com.heima.trade.listen;

import com.heima.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class listenDirectQueue2markOrderPaySuccess {
    private final IOrderService iOrderService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "pay.queue", durable = "true"),
            exchange = @Exchange(name = "hmall.direct_exchange", type = "direct"),
            key = "pay.success"
    ))
    public void listenDirectQueue2markOrderPaySuccessFunc(Long id) throws Exception{
        iOrderService.markOrderPaySuccess(id);
    }
}
