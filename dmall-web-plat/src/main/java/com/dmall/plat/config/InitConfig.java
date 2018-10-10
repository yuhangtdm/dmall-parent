package com.dmall.plat.config;

import com.dmall.web.common.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;

/**
 * @author: yuhang
 * @date: 2018/10/10
 */
@Configuration
public class InitConfig implements CommandLineRunner {

    @Autowired
    private QiniuUtil qiniuUtil;

    @Override
    public void run(String... args) throws Exception {
        //qiniuUtil.uploadFile("E:\\下载\\图片\\xiaolu.jpg");
        // qiniuUtil.uploadFile(new FileInputStream(new File("E:\\下载\\图片\\meinv.jpg")),"Fiwx3JZqRwQqZ2OVcniFQKWJSlda");
    }
}
