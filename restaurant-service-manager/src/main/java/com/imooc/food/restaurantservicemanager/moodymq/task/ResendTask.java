package com.imooc.food.restaurantservicemanager.moodymq.task;

import com.imooc.food.restaurantservicemanager.moodymq.po.TransMessagePO;
import com.imooc.food.restaurantservicemanager.moodymq.sender.TransMessageSender;
import com.imooc.food.restaurantservicemanager.moodymq.service.TransMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName: ResendTask
 * @description:
 * @date: 2022/9/23 0023 星期五 11:07
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@EnableScheduling
@Configuration
@Component
@Slf4j
public class ResendTask {

    @Autowired
    TransMessageService transMessageService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    TransMessageSender transMessageSender;

    @Value("${moodymq.resendTimes}")
    Integer resendTimes;

    @Scheduled(fixedDelayString = "${moodymq.resendFreq}")
    public void resendMessage() {
        //log.info("重发消息定时任务-查询数据库应发未发消息");
        List<TransMessagePO> messagePOS = transMessageService.listReadyMessages();
        log.info("重发消息定时任务-查询到的应发未发消息集合:{}", messagePOS);

        for (TransMessagePO po : messagePOS) {
            log.info("重发消息定时任务-单条信息内容: po:{}", po);
            log.info("重发消息定时任务-id:{},重发次数:{}", po.getId(),po.getSequence());
            if (po.getSequence() > resendTimes) {
                log.error("重发消息定时任务-重发次数超出上限,放弃id:{}",po.getId());
                transMessageService.messageDead(po.getId());
                continue;
            }
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message message = new Message(po.getPayload().getBytes(), messageProperties);
            message.getMessageProperties().setMessageId(po.getId());
            rabbitTemplate.convertAndSend(
                    po.getExchange(),
                    po.getRoutingKey(),
                    message,
                    new CorrelationData(po.getId()));

            log.info("重发消息定时任务-重新发送, ID:{}", po.getId());
            transMessageService.messageResend(po.getId());
        }
    }
}
