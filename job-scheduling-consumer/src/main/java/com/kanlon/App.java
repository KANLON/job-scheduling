package com.kanlon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author zhangcanlong
 * @since 2019-04-09
 **/
@MapperScan("com.kanlon.mapper")
@SpringBootApplication
@EnableAsync
@EnableEurekaClient
@EnableDiscoveryClient
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
