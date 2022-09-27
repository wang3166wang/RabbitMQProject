package com.imooc.food.orderservicemanager.moodymq.service;

import com.imooc.food.orderservicemanager.moodymq.dao.TransMessageDao;
import com.imooc.food.orderservicemanager.moodymq.enummeration.TransMessageType;
import com.imooc.food.orderservicemanager.moodymq.po.TransMessagePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: TransMessageServiceImpl
 * @description:
 * @date: 2022/9/23 0023 星期五 11:15
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Service
public class TransMessageServiceImpl implements TransMessageService {
    @Autowired
    TransMessageDao transMessageDao;

    @Value("${moodymq.service}")
    String serviceName;

    @Override
    public TransMessagePO messageSendReady(String exchange, String routingKey, String body) {
        final String messageId = UUID.randomUUID().toString();
        TransMessagePO transMessagePO = new TransMessagePO();
        transMessagePO.setId(messageId);
        transMessagePO.setService(serviceName);
        transMessagePO.setExchange(exchange);
        transMessagePO.setRoutingKey(routingKey);
        transMessagePO.setPayload(body);
        transMessagePO.setDate(new Date());
        transMessagePO.setSequence(0);
        transMessagePO.setType(TransMessageType.SEND);
        transMessageDao.insert(transMessagePO);
        return transMessagePO;
    }

    @Override
    public void messageSendSuccess(String id) {
        transMessageDao.delete(id, serviceName);
    }

    @Override
    public TransMessagePO messageSendReturn(String id, String exchange, String routingKey, String body) {

        return messageSendReady(exchange, routingKey, body);
    }

    @Override
    public List<TransMessagePO> listReadyMessages() {
        return transMessageDao.selectByTypeAndService(
                TransMessageType.SEND.toString(), serviceName
        );
    }

    @Override
    public void messageResend(String id) {
        TransMessagePO transMessagePO = transMessageDao.selectByIdAndService(id, serviceName);
        transMessagePO.setSequence(transMessagePO.getSequence() + 1);
        transMessageDao.update(transMessagePO);
    }

    @Override
    public void messageDead(String id) {
        TransMessagePO transMessagePO = transMessageDao.selectByIdAndService(id, serviceName);
        transMessagePO.setType(TransMessageType.DEAD);
        transMessageDao.update(transMessagePO);
    }

    @Override
    public void messageDead(String id, String exchange, String routingKey, String queue, String body) {
        TransMessagePO transMessagePO = new TransMessagePO();
        transMessagePO.setId(id);
        transMessagePO.setService(serviceName);
        transMessagePO.setExchange(exchange);
        transMessagePO.setRoutingKey(routingKey);
        transMessagePO.setQueue(queue);
        transMessagePO.setPayload(body);
        transMessagePO.setDate(new Date());
        transMessagePO.setSequence(0);
        transMessagePO.setType(TransMessageType.DEAD);
        transMessageDao.insert(transMessagePO);
    }

    @Override
    public TransMessagePO messageReceiveReady(
            String id, String exchange, String routingKey, String queue, String body) {

        TransMessagePO transMessagePO =
                transMessageDao.selectByIdAndService(id, serviceName);
        if (null == transMessagePO) {
            transMessagePO = new TransMessagePO();
            transMessagePO.setId(id);
            transMessagePO.setService(serviceName);
            transMessagePO.setExchange(exchange);
            transMessagePO.setRoutingKey(routingKey);
            transMessagePO.setQueue(queue);
            transMessagePO.setPayload(body);
            transMessagePO.setDate(new Date());
            transMessagePO.setSequence(0);
            transMessagePO.setType(TransMessageType.RECEIVE);
            transMessageDao.insert(transMessagePO);
        } else {
            transMessagePO.setSequence(transMessagePO.getSequence() + 1);
            transMessageDao.update(transMessagePO);
        }
        return transMessagePO;
    }

    @Override
    public void messageReceiveSuccess(String id) {
        transMessageDao.delete(id, serviceName);
    }
}
