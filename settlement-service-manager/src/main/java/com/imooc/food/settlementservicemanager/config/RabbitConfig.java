package com.imooc.food.settlementservicemanager.config;

import com.imooc.food.settlementservicemanager.service.OrderMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RabbitConfig
 * @description:
 * @date: 2022/9/2 0002 星期五 12:27
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
@Configuration
public class RabbitConfig {
    @Bean
    public Exchange exchange1() {
        return new FanoutExchange("exchange.order.settlement");
    }

    @Bean
    public Queue queue1() {
        return new Queue("queue.settlement",true, false, false, null);
    }

    @Bean
    public Binding binding1() {
        return new Binding(
                "queue.settlement",
                Binding.DestinationType.QUEUE,
                "exchange.order.settlement",
                "key.settlement",
                null);
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory, OrderMessageService orderMessageService) {

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("queue.settlement");
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(orderMessageService);
        return container;
    }
}
