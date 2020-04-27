package com.qf.consignment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = "com.qf")
@MapperScan("com.qf.mapper")
@EnableCaching
public class ConsignmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsignmentApplication.class, args);
    }

}
