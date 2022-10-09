package com.imooc.food.settlementservicemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.food.settlementservicemanager.dao.SettlementDao;
import com.imooc.food.settlementservicemanager.dto.OrderMessageDTO;
import com.imooc.food.settlementservicemanager.enummeration.SettlementStatus;
import com.imooc.food.settlementservicemanager.moodymq.listener.AbstractMessageListener;
import com.imooc.food.settlementservicemanager.moodymq.sender.TransMessageSender;
import com.imooc.food.settlementservicemanager.po.SettlementPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName: OrderMessageService
 * @description:
 * @date: 2022/9/2 0002 星期五 16:21
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */

@Slf4j
@Service
public class OrderMessageService extends AbstractMessageListener {
    @Autowired
    SettlementDao settlementDao;
    @Autowired
    SettlementService settlementService;
    @Autowired
    private TransMessageSender transMessageSender;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void receviceMessage(Message message) {

        String body = new String(message.getBody());

        log.info("Accept Routing Message");
        log.info("messageBody:{}", body);

        try {
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(body, OrderMessageDTO.class);
            SettlementPO settlementPO = new SettlementPO();
            settlementPO.setAmount(orderMessageDTO.getPrice());
            settlementPO.setDate(new Date());
            settlementPO.setOrderId(orderMessageDTO.getOrderId());
            Integer transactionId = settlementService.settlement(orderMessageDTO.getAccountId(), orderMessageDTO.getPrice());
            settlementPO.setStatus(SettlementStatus.SUCCESS);
            settlementPO.setTransactionId(transactionId);
            settlementDao.insert(settlementPO);
            orderMessageDTO.setSettlementId(transactionId);
            transMessageSender.send(
                    "exchange.settlement.order",
                    "key.order",
                    orderMessageDTO
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

/*    DeliverCallback deliverCallback = (consumerTag, message) -> {
        String messageBody = new String(message.getBody());
        log.info("deliverCallback:messageBody:{}", messageBody);
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try {
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(messageBody,
                    OrderMessageDTO.class);
            log.info("handleOrderService:orderSettlementDTO:{}", orderMessageDTO);
            SettlementPO settlementPO = new SettlementPO();
            settlementPO.setAmount(orderMessageDTO.getPrice());
            settlementPO.setDate(new Date());
            settlementPO.setOrderId(orderMessageDTO.getOrderId());
            settlementPO.setStatus(SettlementStatus.SUCCESS);
            settlementPO.setTransactionId(settlementService.settlement(orderMessageDTO.getAccountId(),
                    orderMessageDTO.getPrice()));
            settlementDao.insert(settlementPO);
            orderMessageDTO.setSettlementId(settlementPO.getId());
            log.info("handleOrderService:settlementOrderDTO:{}", orderMessageDTO);

            try (Connection connection = connectionFactory.newConnection();
                 Channel channel = connection.createChannel()) {
                String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
                channel.basicPublish("exchange.settlement.order", "key.order", null, messageToSend.getBytes());
            }
        } catch (JsonProcessingException | TimeoutException e) {
            e.printStackTrace();
        }
    };

    @Async
    public void handleMessage() throws IOException, TimeoutException, InterruptedException {
        log.info("start linstening message");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(
                    "exchange.order.settlement",
                    BuiltinExchangeType.FANOUT,
                    true,
                    false,
                    null);


            channel.queueDeclare(
                    "queue.settlement",
                    true,
                    false,
                    false,
                    null);

            channel.queueBind(
                    "queue.settlement",
                    "exchange.order.settlement",
                    "key.settlement");


            channel.basicConsume("queue.settlement", true, deliverCallback, consumerTag -> {
            });
            while (true) {
                Thread.sleep(100000);
            }
        }
    }*/
}