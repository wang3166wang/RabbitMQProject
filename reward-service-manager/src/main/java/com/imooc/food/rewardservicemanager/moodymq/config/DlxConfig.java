package com.imooc.food.rewardservicemanager.moodymq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: DlxConfig
 * @description:
 * @date: 2022/9/27 0027 星期二 16:08
 * @Company: 鑫开拓电子商务有限公司
 * @author: wsq
 * @version: V1.0
 * @Copyright: Copyright (c) 2022
 */
@Configuration
@ConditionalOnProperty("moodymq.dlxEnabled")
public class DlxConfig {
    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange("exchange.dlx");
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue("queue.dlx",
                true,
                false,
                false);
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with("#");
    }
}
