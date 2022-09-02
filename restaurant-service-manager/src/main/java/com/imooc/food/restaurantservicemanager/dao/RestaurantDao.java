package com.imooc.food.restaurantservicemanager.dao;

import com.imooc.food.restaurantservicemanager.po.RestaurantPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: RestaurantDao
 * @description:
 * @date: 2022/9/2 0002 星期五 14:41
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface RestaurantDao {

    @Select("SELECT id,name,address,status,settlement_id settlementId,date FROM restaurant WHERE id = #{id}")
    RestaurantPO selsctRestaurant(Integer id);
}
