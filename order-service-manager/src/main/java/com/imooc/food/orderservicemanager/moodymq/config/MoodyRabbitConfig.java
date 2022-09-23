package com.imooc.food.orderservicemanager.moodymq.config;

import com.imooc.food.orderservicemanager.moodymq.service.TransMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: MoodyRabbitConfig
 * @description:
 * @date: 2022/9/23 0023 星期五 10:42
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Configuration
@Slf4j
public class MoodyRabbitConfig {

    @Autowired
    TransMessageService transMessageService;

    @Value("${moodymq.host}")
    String host;
    @Value("${moodymq.port}")
    int port;
    @Value("${moodymq.username}")
    String username;
    @Value("${moodymq.password}")
    String password;
    @Value("${moodymq.vhost}")
    String vhost;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(vhost);
        //开启消息成功路由到交换机后触发回调方法
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        //开启失败消息触发回调方法
        connectionFactory.setPublisherReturns(true);
        connectionFactory.createConnection();
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Bean
    public RabbitTemplate customRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("correlationData:{}, ack:{}, cause:{}",
                    correlationData, ack, cause);
            if (ack && null != correlationData) {
                String messageId = correlationData.getId();
                log.info("消息已经正确投递到交换机， id:{}", messageId);
                transMessageService.messageSendSuccess(messageId);
            }else {
                log.error("消息投递至交换机失败，correlationData:{}", correlationData);
            }
        });

        rabbitTemplate.setReturnsCallback(returned -> {log.error("消息无法路由！message:{}, replyCode:{} replyText:{} exchange:{} routingKey:{}",returned.getMessage(), returned.getReplyCode(), returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
//        rabbitTemplate.setReturnsCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            log.error("消息无法路由！message:{}, replyCode:{} replyText:{} exchange:{} routingKey:{}",
//                    message, replyCode, replyText, exchange, routingKey);
            transMessageService.messageSendReturn(
                    returned.getMessage().getMessageProperties().getMessageId(),
                    returned.getExchange(),
                    returned.getRoutingKey(),
                    new String(returned.getMessage().getBody())
            );
        });
        return rabbitTemplate;
    }

}
