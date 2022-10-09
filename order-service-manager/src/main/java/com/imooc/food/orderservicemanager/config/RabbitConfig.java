package com.imooc.food.orderservicemanager.config;

import com.imooc.food.orderservicemanager.service.OrderMessageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

//    @Autowired
//    public void startListenMessage() throws IOException, TimeoutException, InterruptedException {
//        orderMessageService.handleMessage();
//    }

    /*---------------------restaurant---------------------*/
    @Bean
    public Exchange exchange1() {
        return new DirectExchange("exchange.order.restaurant");
    }

    @Bean
    public Queue queue1() {
        return new Queue("queue.order");
    }

    @Bean
    public Binding binding1() {
        return new Binding(
                "queue.order",
                Binding.DestinationType.QUEUE,
                "exchange.order.restaurant",
                "key.order",
                null);
    }

    /*---------------------deliveryman---------------------*/
    @Bean
    public Exchange exchange2() {
        return new DirectExchange("exchange.order.deliveryman");
    }

    @Bean
    public Binding binding2() {
        return new Binding(
                "queue.order",
                Binding.DestinationType.QUEUE,
                "exchange.order.deliveryman",
                "key.order",
                null);
    }

    /*---------settlement---------*/
    @Bean
    public Exchange exchange3() {
        return new FanoutExchange("exchange.order.settlement");
    }

    @Bean
    public Exchange exchange4() {
        return new FanoutExchange("exchange.settlement.order");
    }

    @Bean
    public Binding binding3() {
        return new Binding(
                "queue.order",
                Binding.DestinationType.QUEUE,
                "exchange.settlement.order",
                "key.order",
                null);
    }

    /*--------------reward----------------*/
    @Bean
    public Exchange exchange5() {
        return new TopicExchange("exchange.order.reward");
    }

    @Bean
    public Binding binding4() {
        return new Binding(
                "queue.order",
                Binding.DestinationType.QUEUE,
                "exchange.order.reward",
                "key.order",
                null);
    }

    //消息监听容器
    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory, OrderMessageService orderMessageService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        //可配置多个监听队列
        container.setQueueNames("queue.order");
        container.setExposeListenerChannel(true);
        //消费端设置确认模式:手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(orderMessageService);
        return container;
//        SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
//        messageListenerContainer.setQueueNames("queue.order");
//        //并发的消费者数量
//        messageListenerContainer.setConcurrentConsumers(3);
//        messageListenerContainer.setMaxConcurrentConsumers(5);
//        messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
//        MessageListenerAdapter listenerAdapter = new MessageListenerAdapter();
//        listenerAdapter.setDelegate(orderMessageService);
//        Map<String, String> methodMap = new HashMap<>(8);
//        methodMap.put("queue.order", "handleMessage1");
//        listenerAdapter.setQueueOrTagToMethodName(methodMap);
//        messageListenerContainer.setMessageListener(listenerAdapter);
//        return messageListenerContainer;
    }

//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setPassword("guest");
//        connectionFactory.setUsername("guest");
//        //开启消息确认和返回机制，不只要在rabbitTemplate开启，还要在连接工厂这里开启
//        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
//        connectionFactory.setPublisherReturns(true);
//        //有点懒加载的意思，只有开始使用了才开始初始化
//        connectionFactory.createConnection();
//        return connectionFactory;
//    }
//
//    @Bean
//    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
//        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
//        //开启自动启动,不开启的话 前面声明的都不会被自动创建
//        rabbitAdmin.setAutoStartup(true);
//        return rabbitAdmin;
//    }
//
//    @Bean
//    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        //托管，消息确认
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            log.info("message:{}, replyCode:{}, replyText:{}, exchange:{}, routingKey:{}",
//                    message, replyCode, replyText, exchange, routingKey);
//        });
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) ->
//                log.info("correlationData:{}, ack:{}, cause:{}",
//                        correlationData, ack, cause));
//        return rabbitTemplate;
//    }
}
