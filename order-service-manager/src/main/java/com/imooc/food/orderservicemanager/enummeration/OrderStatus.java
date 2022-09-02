package com.imooc.food.orderservicemanager.enummeration;

/**
 * @ClassName: OrderStatus
 * @description:
 * @date: 2022/9/2 0002 星期五 10:20
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
public enum OrderStatus {
    ORDER_CREATING,
    RESTAURANT_CONFIRMED,
    DELIVERYMAN_CONFIRMED,
    SETTLEMENT_CONFIRMED,
    ORDER_CREATED,
    FAILED;
}
