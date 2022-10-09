package com.imooc.food.settlementservicemanager.moodymq.listener;

import com.imooc.food.settlementservicemanager.moodymq.po.TransMessagePO;
import com.imooc.food.settlementservicemanager.moodymq.service.TransMessageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * @ClassName: AbstractMessageListener
 * @description:
 * @date: 2022/9/26 0026 星期一 17:49
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
public abstract class AbstractMessageListener implements ChannelAwareMessageListener {

    @Autowired
    TransMessageService transMessageService;
    @Value("${moodymq.resendTimes}")
    Integer resendTimes;

    public abstract void receviceMessage(Message message) throws IOException;

    @Override
    public void  onMessage(Message message, Channel channel) throws IOException, InterruptedException {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();

        //收到消息后先进行落盘处理
        TransMessagePO transMessagePO = transMessageService.messageReceiveReady(
                        messageProperties.getMessageId(),
                        messageProperties.getReceivedExchange(),
                        messageProperties.getReceivedRoutingKey(),
                        messageProperties.getConsumerQueue(),
                        new String(message.getBody()));

        log.info("收到消息{}, 消费次数{}", messageProperties.getMessageId(),transMessagePO.getSequence());

        try{
            receviceMessage(message);
            //默认情况下，消费端接收消息时，消息会被自动确认（ACK)
            //这里进行手动ACK
            channel.basicAck(deliveryTag , false);
            log.info("收到消息{}，手动ACK",messageProperties.getMessageId());
            //手动确认完后，数据库删除消息
            transMessageService.messageReceiveSuccess(messageProperties.getMessageId());
        }catch (Exception e){
            log.error(e.getMessage(), e);
            if (transMessagePO.getSequence() >= resendTimes){
                log.info("消息{}, 消费次数超出上限，进入死信队列!", messageProperties.getMessageId());
                //关闭重回队列
                channel.basicReject(deliveryTag, false);
            } else {
                Thread.sleep((long)(Math.pow(2, transMessagePO.getSequence())) * 1000);
                //Nack单条消息，开启重回队列，可进行重新消费
                channel.basicNack(deliveryTag, false, true);
            }
        }
    }
}

