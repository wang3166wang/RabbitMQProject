package com.imooc.food.orderservicemanager.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName: 前端创建订单时所需要的参数
 * @description:
 * @date: 2022/9/2 0002 星期五 10:15
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Getter
@Setter
@ToString
public class OrderCreateVO {
    //用户id
    private Integer accountId;
    //地址
    private String address;
    //商品id
    private Integer productId;
}
