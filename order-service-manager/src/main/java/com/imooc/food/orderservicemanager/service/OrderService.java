package com.imooc.food.orderservicemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.food.orderservicemanager.dao.OrderDetailDao;
import com.imooc.food.orderservicemanager.dto.OrderMessageDTO;
import com.imooc.food.orderservicemanager.enummeration.OrderStatus;
import com.imooc.food.orderservicemanager.moodymq.sender.TransMessageSender;
import com.imooc.food.orderservicemanager.po.OrderDetailPO;
import com.imooc.food.orderservicemanager.vo.OrderCreateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @ClassName: OrderService
 * @description:
 * @date: 2022/9/2 0002 星期五 10:35
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderDetailDao orderDetailDao;
    @Autowired
    TransMessageSender transMessageSender;

    public void createOrder(OrderCreateVO orderCreateVO) throws InterruptedException {
        log.info("orderCreateVO:{}", orderCreateVO);
        OrderDetailPO orderPO = new OrderDetailPO();
        orderPO.setAddress(orderCreateVO.getAddress());
        orderPO.setAccountId(orderCreateVO.getAccountId());
        orderPO.setProductId(orderCreateVO.getProductId());
        orderPO.setStatus(OrderStatus.ORDER_CREATING);
        orderPO.setDate(new Date());
        orderDetailDao.insert(orderPO);


        OrderMessageDTO orderMessageDTO = new OrderMessageDTO();
        orderMessageDTO.setOrderId(orderPO.getId());
        orderMessageDTO.setProductId(orderPO.getProductId());
        orderMessageDTO.setAccountId(orderCreateVO.getAccountId());

        transMessageSender.send(
                "exchange.order.restaurant",
                "key.restaurant",
                orderMessageDTO
        );

//        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
//        MessageProperties messageProperties = new MessageProperties();
//        //过期时间
//        messageProperties.setExpiration("15000");
//        Message message = new Message(messageToSend.getBytes(), messageProperties);
//        CorrelationData correlationData = new CorrelationData();
//        correlationData.setId(orderPO.getId().toString());
        //传递额外参数时 用这个比较好
//        rabbitTemplate.send(
//                "exchange.order.restaurant",
//                "key.restaurant",
//                message, correlationData
//        );
        //不传递额外参数时 用这个比较简单
//        rabbitTemplate.convertAndSend(
//                "exchange.order.restaurant",
//                "key.restaurant",
//                messageToSend);

        //Template模板在于都会调用execute方法

//        transMessageSender.send(
//                "exchange.order.restaurant",
//                "key.restaurant",
//                messageToSend);

        log.info("order微服务生成订单，转发至restaurant微服务");

        Thread.sleep(1000);

    }

}
