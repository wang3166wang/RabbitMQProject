package com.imooc.food.orderservicemanager.dao;

import com.imooc.food.orderservicemanager.po.OrderDetailPO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: OrderDetailDao
 * @description:
 * @date: 2022/9/2 0002 星期五 10:29
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface OrderDetailDao {
    @Insert("INSERT INTO order_detail (status, address, account_id, product_id, deliveryman_id, settlement_id, " +
            "reward_id, price, date) VALUES(#{status}, #{address},#{accountId},#{productId},#{deliverymanId}," +
            "#{settlementId}, #{rewardId},#{price}, #{date})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(OrderDetailPO orderDetailPO);

    @Update("update order_detail set status =#{status}, address =#{address}, account_id =#{accountId}, " +
            "product_id =#{productId}, deliveryman_id =#{deliverymanId}, settlement_id =#{settlementId}, " +
            "reward_id =#{rewardId}, price =#{price}, date =#{date} where id=#{id}")
    void update(OrderDetailPO orderDetailPO);

    @Select("SELECT id,status,address,account_id accountId, product_id productId,deliveryman_id deliverymanId," +
            "settlement_id settlementId,reward_id rewardId,price, date FROM order_detail WHERE id = #{id}")
    OrderDetailPO selectOrder(Integer id);
}
