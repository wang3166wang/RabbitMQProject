package com.imooc.food.settlementservicemanager.dao;

import com.imooc.food.settlementservicemanager.po.SettlementPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: SettlementDao
 * @description:
 * @date: 2022/9/2 0002 星期五 16:18
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface SettlementDao {

    @Insert("INSERT INTO settlement (order_id, transaction_id, amount, status, date) " +
            "VALUES(#{orderId}, #{transactionId}, #{amount}, #{status}, #{date})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(SettlementPO settlementPO);
}
