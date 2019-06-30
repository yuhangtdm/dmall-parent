package com.dmall.crawler;

import com.dmall.crawler.jd.JdPhoneCrawler;

/**
 * @author: yuhang
 * @date: 2019/1/19
 */
public class Main {

    public static void main(String[] args) {
        new Thread(new JdPhoneCrawler()).start();
    }
}
