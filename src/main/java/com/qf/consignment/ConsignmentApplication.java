package com.qf.consignment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.qf")
@MapperScan("com.qf.mapper")
public class ConsignmentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsignmentApplication.class, args);
    }

}
