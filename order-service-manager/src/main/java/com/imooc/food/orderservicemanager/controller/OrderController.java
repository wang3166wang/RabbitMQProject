package com.imooc.food.orderservicemanager.controller;

import com.imooc.food.orderservicemanager.service.OrderService;
import com.imooc.food.orderservicemanager.vo.OrderCreateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: OrderController
 * @description:
 * @date: 2022/9/2 0002 星期五 11:43
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
@RestController
@RequestMapping("api/v1")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/orders")
    public void createOrder(@RequestBody OrderCreateVO orderCreateDTO) throws IOException, TimeoutException {
        log.info("createOrder:orderCreateDTO:{}", orderCreateDTO);
        orderService.createOrder(orderCreateDTO);
    }
}
