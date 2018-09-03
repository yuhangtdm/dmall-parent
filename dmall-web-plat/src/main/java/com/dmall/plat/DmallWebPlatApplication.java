package com.dmall.plat;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan(basePackages = {"com.dmall.product.mapper"})
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.dmall"})
@EnableCaching
public class DmallWebPlatApplication {

    public static void main(String[] args) {
        SpringApplication.run(DmallWebPlatApplication.class, args);

    }


}
