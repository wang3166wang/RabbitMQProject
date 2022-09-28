package com.imooc.food.restaurantservicemanager.config;

import com.imooc.food.restaurantservicemanager.service.OrderMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
    @Autowired
    OrderMessageService orderMessageService;

    /*---------------------restaurant---------------------*/
    @Bean
    public Exchange exchange1() {
        return new DirectExchange("exchange.order.restaurant");
    }

    @Bean
    public Queue queue1() {
        return new Queue("queue.restaurant",true, false, false, null);
    }

    @Bean
    public Binding binding1() {
        return new Binding(
                "queue.restaurant",
                Binding.DestinationType.QUEUE,
                "exchange.order.restaurant",
                "key.restaurant",
                null);
    }
    //消息监听容器
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory, OrderMessageService orderMessageService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames("queue.restaurant");
        container.setExposeListenerChannel(true);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(orderMessageService);
        return container;

//    @Autowired
//    public void startListenMessage() throws Exception {
//        orderMessageService.handleMessage();
//    }
    }
}
