package com.dmall.quartz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author: yuhang
 * @date: 2018/12/2
 */
@Configuration
public class QuartzConfig {

    public SchedulerFactoryBean  quartzScheduler(){
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        factoryBean.setAutoStartup(true);
        factoryBean.setOverwriteExistingJobs(true);

        return factoryBean;
    }
}
