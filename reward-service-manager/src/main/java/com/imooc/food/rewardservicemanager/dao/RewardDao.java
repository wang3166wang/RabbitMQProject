package com.imooc.food.rewardservicemanager.dao;

import com.imooc.food.rewardservicemanager.po.RewardPO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: RewardDao
 * @description:
 * @date: 2022/9/2 0002 星期五 17:14
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface RewardDao {

    @Insert("INSERT INTO reward (order_id, amount, status, date) VALUES(#{orderId}, #{amount}, #{status}, #{date})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(RewardPO rewardPO);
}
