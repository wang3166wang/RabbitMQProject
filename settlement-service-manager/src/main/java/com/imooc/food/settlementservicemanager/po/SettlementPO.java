package com.imooc.food.settlementservicemanager.po;

import com.imooc.food.settlementservicemanager.enummeration.SettlementStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: SettlementPO
 * @description:
 * @date: 2022/9/2 0002 星期五 16:18
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Getter
@Setter
@ToString
public class SettlementPO {
    private Integer id;
    private Integer orderId;
    private Integer transactionId;
    private SettlementStatus status;
    private BigDecimal amount;
    private Date date;
}
