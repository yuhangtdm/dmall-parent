package com.dmall.crawler.jd;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dmall.common.utils.StringUtil;
import com.dmall.crawler.util.HttpClientUtil;
import com.dmall.product.entity.Sku;
import com.dmall.product.mapper.SkuMapper;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: yuhang
 * @date: 2019/1/19
 */
public class JdPhoneCrawler extends JdBaseCrawler {

    private static Logger logger = LoggerFactory.getLogger(JdPhoneCrawler.class);

    private static final String BASE_URL = "https://list.jd.com/list.html?cat=9987,653,655&page=%s";
    private static final String TOTAL_PAGE = "#J_topPage";
    private static final String SKU_LIST = "#plist .gl-item";
    private static final String SKU_ID = "data-sku";
    private static final String SKU_NAME = ".p-name";
    private static final String SKU_IMG = ".p-img";
    private static final String SKU_PRICE = ".p-price";
    private static final String GET_PRICE_URL = "https://p.3.cn/prices/mgets?skuIds=%s";

    private SkuMapper skuMapper;


    @Override
    protected Integer getTotalPage(String content) {
        String totalPageContent = Jsoup.parse(content).select(TOTAL_PAGE).text();
        String[] split = totalPageContent.split("\\D+");
        return Integer.valueOf(split[1]);
    }

    @Override
    protected String getRequestUrl(int i) {
        return String.format(BASE_URL, i);
    }

    @Override
    protected void doParse(String pageContent) {
        Elements productList = Jsoup.parse(pageContent).select(SKU_LIST);
        List<Sku> skuList = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        for (Element element : productList){
            Sku sku = new Sku();
            Element item = element.child(0);
            String skuId =item.attr(SKU_ID);
            String skuName = item.select(SKU_NAME).select("em").text();
            String imgUrl = item.select(SKU_IMG).select("img").attr("src");
            String lazyUrl = item.select(SKU_IMG).select("img").attr("data-lazy-img");
            imgUrl = "https:"+(StringUtil.isBlank(imgUrl) ? lazyUrl : imgUrl);
            BigDecimal price = getPrice(skuId);
            sku.setId(Long.valueOf(skuId));
            sku.setSkuName(skuName);
            sku.setSkuMainPic(imgUrl);
            sku.setPrice(price.doubleValue());
            skuList.add(sku);
            sb.append(JSONObject.toJSONString(sku)+"\n");
        }
        File file = new File("D:\\jd.txt");
        InputStream inputStream = IOUtils.toInputStream(sb.toString());
        FileOutputStream fileOutputStream = null;
        byte[] bytes = new byte[1024];
        try {
            fileOutputStream = new FileOutputStream(file);
            int len =0;
            while ((len=inputStream.read(bytes))!=-1){
                fileOutputStream.write(bytes,0,len);
            }
        }catch (Exception e){

        }finally {
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

    private BigDecimal getPrice(String skuId) {
        String priceStr = HttpClientUtil.sendHttpGet(String.format(GET_PRICE_URL, "J_" + skuId));
        JSONArray array = JSONArray.parseArray(priceStr);
        return  array.getJSONObject(0).getBigDecimal("p");

    }
}
