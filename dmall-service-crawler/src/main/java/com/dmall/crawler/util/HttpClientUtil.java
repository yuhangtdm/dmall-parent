package com.dmall.crawler.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author:yuhang
 * @Date:2018/11/2
 */
public class HttpClientUtil {

    private HttpClientUtil(){}

    // 连接超时时间 默认30秒  30秒连接不上自动返回
    private static final int CONNECT_TIME=30*1000;

    // 读取超时时间 如果访问一个接口 多少时间内未返回数据 就直接放弃此次调用
    private static final int SOCKET_TIME=30*1000;

    //设置从connect Manager获取Connection 超时时间
    private static final int CONNECTION_REQUEST_TIME = 30*1000;


    /**
     * 连接时间参数
     */
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(CONNECT_TIME)
            .setSocketTimeout(SOCKET_TIME)
            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIME)
            .build();


    /**
     * 执行post请求
     * @param httpUrl
     * @return
     */
    public String sendHttpPost(String httpUrl) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        return sendHttpPost(httpPost);
    }

    /**
     * 执行带参数的post请求
     * @param httpUrl 地址
     * @param params 参数 (格式:key1=value1&key2=value2)
     * @retur
     */
    public String sendHttpPost(String httpUrl, String params) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        try {
            //设置参数
            StringEntity stringEntity = new StringEntity(params, "UTF-8");
            stringEntity.setContentType("application/x-www-form-urlencoded");
            httpPost.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送带参数的 post请求
     * @param httpUrl 地址
     * @param maps 参数
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps) {
        // 创建httpPost
        HttpPost httpPost = new HttpPost(httpUrl);
        // 创建参数队列
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (String key : maps.keySet()) {
            nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 post请求（带文件）
     * @param httpUrl 地址
     * @param maps 参数
     * @param fileLists 附件
     */
    public String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists) {
        HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
        MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
        for (String key : maps.keySet()) {
            meBuilder.addPart(key, new StringBody(maps.get(key), ContentType.TEXT_PLAIN));
        }
        for(File file : fileLists) {
            FileBody fileBody = new FileBody(file);
            meBuilder.addPart("files", fileBody);
        }
        HttpEntity reqEntity = meBuilder.build();
        httpPost.setEntity(reqEntity);
        return sendHttpPost(httpPost);
    }

    /**
     * 发送 get请求
     */
    public static String sendHttpGet(String url){
        HttpGet httpGet=new HttpGet(url);
        return sendHttpGet(httpGet);
    }

    /**
     * 发送 get请求Https
     * @param httpUrl
     */
    public String sendHttpsGet(String httpUrl) {
        HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
        return sendHttpsGet(httpGet);
    }


    /**
     * 发送http的get请求
     */
    private static String sendHttpGet(HttpGet httpGet){
        CloseableHttpClient client=null;
        CloseableHttpResponse response=null;
        HttpEntity httpEntity=null;
        String responseContent=null;
       try {
           // 创建默认的客户端
           client=HttpClients.createDefault();
           httpGet.setConfig(requestConfig);
           response = client.execute(httpGet);
           httpEntity = response.getEntity();
           responseContent= EntityUtils.toString(httpEntity,"utf-8");
       }catch (Exception e){
            try {
                if(response!=null) {
                    response.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
       }
        return responseContent;
    }

    /**
     * 发送http的post请求
     */
    private static String sendHttpPost(HttpPost httpPost){
        CloseableHttpClient client=null;
        CloseableHttpResponse response=null;
        HttpEntity entity=null;
        String responseContent=null;
        try {
            // 默认的方式创建client
            client=HttpClients.createDefault();
            httpPost.setConfig(requestConfig);
            response=client.execute(httpPost);
            entity = response.getEntity();
            return IOUtils.toString(entity.getContent());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(response!=null) {
                    response.close();
                }
                if(client!=null){
                    client.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return responseContent;
    }

    /**
     * 发送Get请求Https
     */
    private String sendHttpsGet(HttpGet httpGet) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = null;
        String responseContent = null;
        try {
            // 创建默认的httpClient实例.
            PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(new URL(httpGet.getURI().toString()));
            DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
            httpClient = HttpClients.custom().setSSLHostnameVerifier(hostnameVerifier).build();
            httpGet.setConfig(requestConfig);
            // 执行请求
            response = httpClient.execute(httpGet);
            entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseContent;
    }

    public static String sendBrowser(){
        //创建httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //代理对象
//        HttpHost proxy = new HttpHost("127.0.0.1", 8888, "http");
        //配置对象
        RequestConfig config = RequestConfig.custom().build();
        //创建请求方法的实例， 并指定请求url
        HttpGet httpget=new HttpGet("http://www.qq.com");
        //使用配置
        httpget.setConfig(config);
        //设置请求头
        httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpget.setHeader("Accept-Encoding", "gzip, deflate");
        httpget.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.91 Safari/537.36");
        String responseContent=null;
        try {
            CloseableHttpResponse response=httpClient.execute(httpget);
            HttpEntity entity=response.getEntity();
            responseContent=EntityUtils.toString(entity, "utf-8");
            System.out.println(responseContent);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return responseContent;
    }

}
