package com.imooc.food.orderservicemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.imooc.food.orderservicemanager.dao.OrderDetailDao;
import com.imooc.food.orderservicemanager.dto.OrderMessageDTO;
import com.imooc.food.orderservicemanager.enummeration.OrderStatus;
import com.imooc.food.orderservicemanager.moodymq.listener.AbstractMessageListener;
import com.imooc.food.orderservicemanager.moodymq.sender.TransMessageSender;
import com.imooc.food.orderservicemanager.po.OrderDetailPO;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName: OrderMessageService
 * @description:
 * @date: 2022/9/2 0002 星期五 10:37
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
@Service
public class OrderMessageService extends AbstractMessageListener {

    @Autowired
    TransMessageSender transMessageSender;
    @Autowired
    private OrderDetailDao orderDetailDao;

    ObjectMapper objectMapper = new ObjectMapper();

//    @Autowired
//    RabbitTemplate rabbitTemplate;

//    @RabbitListener(
//            //containerFactory = "rabbitListenerContainerFactory",
//            //queues = "queue.order",
//            //admin = "rabbitAdmin",
//            bindings = {
//                    @QueueBinding(
//                            value = @Queue(name = "${imooc.order-queue}",
//                                    arguments = {
//                                            //@Argument(name = "x-message-ttl", value = "1000", type = "java.lang.Integer"),
//                                            //@Argument(name = "x-dead-letter-exchange", value = "aaaaa"),
//                                            //@Argument(name = "x-dead-letter-routing-key", value = "bbbb")
//                                    }),
//                            exchange = @Exchange(name = "exchange.order.restaurant", type = ExchangeTypes.DIRECT),
//                            key = "key.order"
//                    ),
//                    @QueueBinding(
//                            value = @Queue(name = "${imooc.order-queue}"),
//                            exchange = @Exchange(name = "exchange.order.deliveryman", type = ExchangeTypes.DIRECT),
//                            key = "key.order"
//                    ),
//                    @QueueBinding(
//                            value = @Queue(name = "${imooc.order-queue}"),
//                            exchange = @Exchange(name = "exchange.settlement.order", type = ExchangeTypes.FANOUT),
//                            key = "key.order"
//                    ),
//                    @QueueBinding(
//                            value = @Queue(name = "${imooc.order-queue}"),
//                            exchange = @Exchange(name = "exchange.order.reward", type = ExchangeTypes.TOPIC),
//                            key = "key.order"
//                    )
//            }
//    )

    @Override
    public void receviceMessage(Message message) throws IOException {

//    }
//    public void handleMessage(@Payload Message message) throws IOException {
        log.info("Accept Routing Message");
        log.info("message:{}", new String(message.getBody()));
//        ConnectionFactory connectionFactory = new ConnectionFactory();
//        connectionFactory.setHost("localhost");
        try {
            OrderMessageDTO orderMessageDTO = objectMapper.readValue(message.getBody(),
                    OrderMessageDTO.class);
            OrderDetailPO orderPO = orderDetailDao.selectOrder(orderMessageDTO.getOrderId());

            switch (orderPO.getStatus()) {

                case ORDER_CREATING:
                    if (orderMessageDTO.getConfirmed() && null != orderMessageDTO.getPrice()) {
                        orderPO.setStatus(OrderStatus.RESTAURANT_CONFIRMED);
                        orderPO.setPrice(orderMessageDTO.getPrice());
                        orderDetailDao.update(orderPO);

                        transMessageSender.send(
                                "exchange.order.deliveryman",
                                "key.deliveryman",
                                orderMessageDTO
                        );

//                        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);

//                        //这是最底层的发消息方法，下面rabbitTemplate的方法对底层方法进行了封装
//                        try (Connection connection = connectionFactory.newConnection();
//                             Channel channel = connection.createChannel()) {
//                            String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
//                            channel.basicPublish("exchange.order.deliveryman", "key.deliveryman", null,
//                                    messageToSend.getBytes());
//                        }

//                        //不传递额外参数时 用这个比较简单
//                        rabbitTemplate.convertAndSend(
//                                "exchange.order.deliveryman",
//                                "key.deliveryman",
//                                messageToSend);
                        log.info("restaurant微服务已确认，转发至deliveryman微服务");
                    } else {
                        orderPO.setStatus(OrderStatus.FAILED);
                        orderDetailDao.update(orderPO);
                        log.info("restaurant微服务未确认，订单创建失败");
                    }
                    break;
                case RESTAURANT_CONFIRMED:
                    if (null != orderMessageDTO.getDeliverymanId()) {
                        orderPO.setStatus(OrderStatus.DELIVERYMAN_CONFIRMED);
                        orderPO.setDeliverymanId(orderMessageDTO.getDeliverymanId());
                        orderDetailDao.update(orderPO);


                        transMessageSender.send("exchange.order.settlement","key.settlement",orderMessageDTO);
//                        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
//                        rabbitTemplate.convertAndSend(
//                                "exchange.order.settlement",
//                                "key.settlement",
//                                messageToSend);
                        log.info("deliveryman微服务已确认，转发至settlement微服务");

                    } else {
                        orderPO.setStatus(OrderStatus.FAILED);
                        orderDetailDao.update(orderPO);
                        log.info("deliveryman微服务未确认，订单创建失败");
                    }
                    break;
                case DELIVERYMAN_CONFIRMED:
                    if (null != orderMessageDTO.getSettlementId()) {
                        orderPO.setStatus(OrderStatus.SETTLEMENT_CONFIRMED);
                        orderPO.setSettlementId(orderMessageDTO.getSettlementId());
                        orderDetailDao.update(orderPO);

                        transMessageSender.send("exchange.order.reward","key.reward",orderMessageDTO);

//                        String messageToSend = objectMapper.writeValueAsString(orderMessageDTO);
//                        transMessageSender.send("exchange.order.reward","key.reward",messageToSend);
//                        rabbitTemplate.convertAndSend(
//                                "exchange.order.reward",
//                                "key.reward",
//                                messageToSend);
                        log.info("settlement微服务已确认，转发至reward微服务");

                    } else {
                        orderPO.setStatus(OrderStatus.FAILED);
                        orderDetailDao.update(orderPO);
                        log.info("settlement微服务未确认，订单创建失败");
                    }
                    break;
                case SETTLEMENT_CONFIRMED:
                    if (null != orderMessageDTO.getRewardId()) {
                        orderPO.setStatus(OrderStatus.ORDER_CREATED);
                        orderPO.setRewardId(orderMessageDTO.getRewardId());
                        orderDetailDao.update(orderPO);
                        log.info("reward微服务已确认，下单业务流程结束");
                    } else {
                        orderPO.setStatus(OrderStatus.FAILED);
                        orderDetailDao.update(orderPO);
                        log.info("reward微服务未确认，订单创建失败");
                    }
                    break;
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException();
        }
    }

}
