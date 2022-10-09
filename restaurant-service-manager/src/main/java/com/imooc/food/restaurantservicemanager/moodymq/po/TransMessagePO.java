package com.imooc.food.restaurantservicemanager.moodymq.po;

import com.imooc.food.restaurantservicemanager.moodymq.enummeration.TransMessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName: TransMessagePO
 * @description:
 * @date: 2022/9/23 0023 星期五 11:01
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Getter
@Setter
@ToString
public class TransMessagePO {
    private String id;
    private String service;
    private TransMessageType type;
    private String exchange;
    private String routingKey;
    private String queue;
    private Integer sequence;
    private String payload;
    private Date date;
}
