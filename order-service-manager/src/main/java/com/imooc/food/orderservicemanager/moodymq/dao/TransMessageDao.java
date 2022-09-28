package com.imooc.food.orderservicemanager.moodymq.dao;


import com.imooc.food.orderservicemanager.moodymq.po.TransMessagePO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: TransMessageDao
 * @description:
 * @date: 2022/9/23 0023 星期五 11:03
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Mapper
@Repository
public interface TransMessageDao {

    @Insert("INSERT INTO trans_message (id, type, service, " +
            "exchange, routing_key, queue, sequence, payload," +
            "date) " +
            "VALUES(#{id}, #{type}, #{service},#{exchange}," +
            "#{routingKey},#{queue},#{sequence}, #{payload},#{date})")
    void insert(TransMessagePO transMessagePO);

    @Update("UPDATE trans_message set type=#{type}, " +
            "service=#{service}, exchange =#{exchange},"+
            "routing_key =#{routingKey}, queue =#{queue}, " +
            "sequence =#{sequence}, payload =#{payload}, " +
            "date =#{date} " +
            "where id=#{id} and service=#{service}")
    void update(TransMessagePO transMessagePO);

    @Select("SELECT id, type, service, exchange, " +
            "routing_key routingKey, queue, sequence, " +
            "payload, date " +
            "FROM trans_message " +
            "where id=#{id} and service=#{service}")
    TransMessagePO selectByIdAndService(@Param("id") String id,
                                        @Param("service") String service);

    @Select("SELECT id, type, service, exchange, " +
            "routing_key routingKey, queue, sequence, " +
            "payload, date " +
            "FROM trans_message " +
            "WHERE type = #{type} and service = #{service}")
    List<TransMessagePO> selectByTypeAndService(
            @Param("type")String type,
            @Param("service")String service);

    @Delete("DELETE FROM trans_message " +
            "where id=#{id} and service=#{service}")
    void delete(@Param("id") String id,
                @Param("service") String service);
}
