package com.dmall.crawler.jd;

import com.dmall.crawler.Crawler;
import com.dmall.crawler.util.HttpClientUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: yuhang
 * @date: 2019/1/19
 */
public abstract class JdBaseCrawler  implements Crawler {

    private Logger logger = LoggerFactory.getLogger(JdBaseCrawler.class);
    private static final Integer START_PAGE = 1;

    public void start(){
        String firstPage = getRequestUrl(START_PAGE);
        String content = HttpClientUtil.sendHttpGet(firstPage);
        Integer totalPage = getTotalPage(content);
        logger.info("请求页面:{},总页数:{}",firstPage,totalPage);
        for(int i = START_PAGE; i <= totalPage; i++ ){
            String requestUrl = getRequestUrl(i);
            String pageContent = HttpClientUtil.sendHttpGet(requestUrl);
            doParse(pageContent);
        }
    }




    @Override
    public void run() {
        start();
    }

    /**
     * 获取总页数
     */
    protected abstract Integer getTotalPage(String content);

    /**
     * 获取要爬取的url
     */
    protected abstract String getRequestUrl(int i);

    /**
     * 解析请求的url
     */
    protected abstract void doParse(String pageContent);




}
