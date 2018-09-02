package com.dmall.plat.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author: yuhang
 * @date: 2018/9/1
 */
@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druid(){
        return new DruidDataSource();
    }


}
