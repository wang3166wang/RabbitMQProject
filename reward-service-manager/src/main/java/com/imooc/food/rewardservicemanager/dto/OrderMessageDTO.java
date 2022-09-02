package com.imooc.food.rewardservicemanager.dto;

import com.imooc.food.rewardservicemanager.enummeration.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @ClassName: OrderMessageDTO
 * @description:
 * @date: 2022/9/2 0002 星期五 17:12
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Getter
@Setter
@ToString
public class OrderMessageDTO {
    private Integer orderId;
    private OrderStatus orderStatus;
    private BigDecimal price;
    private Integer deliverymanId;
    private Integer productId;
    private Integer accountId;
    private Integer settlementId;
    private Integer rewardId;
    private BigDecimal rewardAmount;
    private Boolean confirmed;
}
