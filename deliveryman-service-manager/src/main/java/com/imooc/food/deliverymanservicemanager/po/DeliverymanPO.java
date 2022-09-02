package com.imooc.food.deliverymanservicemanager.po;

import com.imooc.food.deliverymanservicemanager.enummeration.DeliverymanStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @ClassName: DeliverymanPO
 * @description:
 * @date: 2022/9/2 0002 星期五 14:57
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Getter
@Setter
@ToString
public class DeliverymanPO {
    private Integer id;
    private String name;
    private String district;
    private DeliverymanStatus status;
    private Date date;
}
