package com.imooc.food.restaurantservicemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.food.restaurantservicemanager.dao.ProductDao;
import com.imooc.food.restaurantservicemanager.dao.RestaurantDao;
import com.imooc.food.restaurantservicemanager.dto.OrderMessageDTO;
import com.imooc.food.restaurantservicemanager.enummeration.ProductStatus;
import com.imooc.food.restaurantservicemanager.enummeration.RestaurantStatus;
import com.imooc.food.restaurantservicemanager.moodymq.listener.AbstractMessageListener;
import com.imooc.food.restaurantservicemanager.moodymq.sender.TransMessageSender;
import com.imooc.food.restaurantservicemanager.po.ProductPO;
import com.imooc.food.restaurantservicemanager.po.RestaurantPO;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName: OrderMessageService
 * @description:
 * @date: 2022/9/2 0002 星期五 14:42
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
@Service
public class OrderMessageService extends AbstractMessageListener {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    TransMessageSender transMessageSender;
    @Autowired
    ProductDao productDao;
    @Autowired
    RestaurantDao restaurantDao;

    @Override
    public void receviceMessage(Message message) {
        String body = new String(message.getBody());

        log.info("Accept Routing Message");
        log.info("messageBody:{}", body);

        try {
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(body, OrderMessageDTO.class);
            ProductPO productPO = productDao.selsctProduct(orderMessageDTO.getProductId());
            log.info("onMessage:productPO:{}", productPO);
            RestaurantPO restaurantPO = restaurantDao.selsctRestaurant(productPO.getRestaurantId());
            log.info("onMessage:restaurantPO:{}", restaurantPO);

            if (ProductStatus.AVALIABLE == productPO.getStatus() && RestaurantStatus.OPEN == restaurantPO.getStatus()) {
                orderMessageDTO.setConfirmed(true);
                orderMessageDTO.setPrice(productPO.getPrice());
            } else {
                orderMessageDTO.setConfirmed(false);
            }
            log.info("sendMessage:restaurantOrderMessageDTO:{}", orderMessageDTO);

            transMessageSender.send(
                    "exchange.order.restaurant",
                    "key.order",
                    orderMessageDTO
            );

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException();
        }

    }

/*    @Async
    public void handleMessage() throws Exception {
        log.info("start linstening message");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(
                    "exchange.order.restaurant",
                    BuiltinExchangeType.DIRECT,
                    true,
                    false,
                    null);

            channel.queueDeclare(
                    "queue.restaurant",
                    true,
                    false,
                    false,
                    null);

            channel.queueBind(
                    "queue.restaurant",
                    "exchange.order.restaurant",
                    "key.restaurant");


            channel.basicConsume("queue.restaurant", true, deliverCallback, consumerTag -> {
            });
            while (true) {
                Thread.sleep(100000);
            }
        }
    }

    DeliverCallback deliverCallback = (consumerTag, message) -> {
        String messageBody = new String(message.getBody());
        log.info("deliverCallback:messageBody:{}", messageBody);
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);

            ProductPO productPO = productDao.selsctProduct(orderMessageDTO.getProductId());
            log.info("onMessage:productPO:{}", productPO);
            RestaurantPO restaurantPO = restaurantDao.selsctRestaurant(productPO.getRestaurantId());
            log.info("onMessage:restaurantPO:{}", restaurantPO);
            if (ProductStatus.AVALIABLE == productPO.getStatus() && RestaurantStatus.OPEN == restaurantPO.getStatus()) {
                orderMessageDTO.setConfirmed(true);
                orderMessageDTO.setPrice(productPO.getPrice());
            } else {
                orderMessageDTO.setConfirmed(false);
            }
            log.info("sendMessage:restaurantOrderMessageDTO:{}", orderMessageDTO);

            //AutoClosable 消息发送完会自动关闭，这样设置的回调配置就不会起作用
            try (Connection connection = connectionFactory.newConnection();
                 Channel channel = connection.createChannel()) {

                //方式1：
//                channel.addReturnListener(new ReturnListener() {
//                    @Override
//                    public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) {
//                        log.info("Message Return: replyCode:{}, replyText:{}, exchange:{},routingKey:{}, properties:{}, body:{}",replyCode,  replyText,  exchange,  routingKey,  properties, new  String(body));
//                    }
//                });
                //方式2：
//                channel.addReturnListener(new ReturnCallback() {
//                    @Override
//                    public void handle(Return returnMessage) { //入参和上述一样，只是经过包装而已
//                        log.info("Message Return: returnMessage:{}", returnMessage);
//                    }
//                });

                String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                channel.basicPublish("exchange.order.restaurant", "key.order", true ,null, messageToSend.getBytes());

//                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    };*/

}
