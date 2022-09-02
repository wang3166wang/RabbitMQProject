package com.imooc.food.deliverymanservicemanager.dao;

import com.imooc.food.deliverymanservicemanager.enummeration.DeliverymanStatus;
import com.imooc.food.deliverymanservicemanager.po.DeliverymanPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: DeliverymanDao
 * @description:
 * @date: 2022/9/2 0002 星期五 14:59
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface DeliverymanDao {

    @Select("SELECT id,name,status,date FROM deliveryman WHERE id = #{id}")
    DeliverymanPO selectDeliveryman(Integer id);

    @Select("SELECT id,name,status,date FROM deliveryman WHERE status = #{status}")
    List<DeliverymanPO> selectAvaliableDeliveryman(DeliverymanStatus status);
}

