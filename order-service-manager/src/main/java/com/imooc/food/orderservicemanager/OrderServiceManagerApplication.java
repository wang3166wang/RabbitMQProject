package com.imooc.food.orderservicemanager;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(value = "com.imooc" , annotationClass = Mapper.class)
@ComponentScan("com.imooc")
@EnableAsync
public class OrderServiceManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceManagerApplication.class, args);
	}

}
