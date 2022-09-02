package com.imooc.food.rewardservicemanager.po;

import com.imooc.food.rewardservicemanager.enummeration.RewardStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: RewardPO
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
public class RewardPO {
    private Integer id;
    private Integer orderId;
    private BigDecimal amount;
    private RewardStatus status;
    private Date date;
}
