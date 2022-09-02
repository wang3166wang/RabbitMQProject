package com.imooc.food.restaurantservicemanager.po;

import com.imooc.food.restaurantservicemanager.enummeration.ProductStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: ProductPO
 * @description:
 * @date: 2022/9/2 0002 星期五 14:38
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Getter
@Setter
@ToString
public class ProductPO {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer restaurantId;
    private ProductStatus status;
    private Date date;
}
