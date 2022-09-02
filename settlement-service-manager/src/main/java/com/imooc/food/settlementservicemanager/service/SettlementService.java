package com.imooc.food.settlementservicemanager.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

/**
 * @ClassName: SettlementService
 * @description:
 * @date: 2022/9/2 0002 星期五 16:22
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Service
public class SettlementService {

    Random rand = new Random(25);

    public Integer settlement(Integer accountId, BigDecimal amount) {
        return rand.nextInt(1000000000);
    }
}
