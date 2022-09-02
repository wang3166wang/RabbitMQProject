package com.imooc.food.settlementservicemanager.config;

import com.imooc.food.settlementservicemanager.service.OrderMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName: RabbitConfig
 * @description:
 * @date: 2022/9/2 0002 星期五 12:27
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Slf4j
@Configuration
public class RabbitConfig {
    @Autowired
    OrderMessageService orderMessageService;

    @Autowired
    public void startListenMessage() throws IOException, TimeoutException, InterruptedException {
        orderMessageService.handleMessage();
    }
}
