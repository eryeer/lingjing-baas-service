package com.onchain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@MapperScan(basePackages = {"com.onchain.mapper"})
@EnableTransactionManagement
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients
@EnableOpenApi
public class BaasServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaasServiceApplication.class);
    }
}
