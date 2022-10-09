package com.imooc.food.restaurantservicemanager.moodymq.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.food.restaurantservicemanager.dto.OrderMessageDTO;
import com.imooc.food.restaurantservicemanager.moodymq.po.TransMessagePO;
import com.imooc.food.restaurantservicemanager.moodymq.service.TransMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TransMessageSender
 * @description:
 * @date: 2022/9/23 0023 星期五 11:36
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Component
@Slf4j
public class TransMessageSender {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    TransMessageService transMessageService;

    public void send(String exchange, String routingKey, OrderMessageDTO payload){
        log.info("send(): exchange:{} routingKey:{} payload:{}",
                exchange, routingKey, payload);
        try {
            ObjectMapper mapper = new ObjectMapper();
            String payloadStr = mapper.writeValueAsString(payload);

            //发送消息前将数据落盘
            TransMessagePO transMessagePO =
                    transMessageService.messageSendReady(
                            exchange,
                            routingKey,
                            payloadStr
                    );

            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message message = new Message(payloadStr.getBytes(), messageProperties);
            message.getMessageProperties().setMessageId(transMessagePO.getId());
            rabbitTemplate.convertAndSend(exchange, routingKey, message,
                    new CorrelationData(transMessagePO.getId()));

            log.info("message sent, ID:{}", transMessagePO.getId());

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

    }
}
