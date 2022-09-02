package com.imooc.food.restaurantservicemanager.dao;

import com.imooc.food.restaurantservicemanager.po.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @ClassName: ProductDao
 * @description:
 * @date: 2022/9/2 0002 星期五 14:40
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface ProductDao {

    @Select("SELECT id,name,price,restaurant_id restaurantId,status,date FROM product WHERE id = #{id}")
    ProductPO selsctProduct(Integer id);
}
