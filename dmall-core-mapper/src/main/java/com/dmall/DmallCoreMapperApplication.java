package com.dmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.dmall.product.mapper"})
public class DmallCoreMapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmallCoreMapperApplication.class, args);
    }

}
