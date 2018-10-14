package com.dmall.web.common.utils;

import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;


/**
 * @author: yuhang
 * @date: 2018/10/10
 */
@Component
public class QiniuUtil {

    private static final Logger logger=LoggerFactory.getLogger(QiniuUtil.class);

    @Value("${qiniu.accessKey}")
    private  String ACCESSKEY;

    @Value("${qiniu.secretKey}")
    private   String SECRETKEY;

    @Value("${qiniu.bucket}")
    private  String  BUCKET;

    @Value("${qiniu.domain}")
    private String DOMAIN;

    //默认华东
    private static Zone zone=Zone.zone0();

    private static final String THUMB_16="imageView2/2/w/16/h/16";
    private static final String THUMB_32="imageView2/2/w/32/h/32";
    private static final String THUMB_48="imageView2/2/w/48/h/48";
    private static final String THUMB_64="imageView2/2/w/64/h/64";
    private static final String THUMB_128="imageView2/2/w/128/h/128";
    private static final String THUMB_256="imageView2/2/w/256/h/256";
    private static final String THUMB_512="imageView2/2/w/512/h/512";
    private static final String THUMB_BASE="imageView2/2/w/%s/h/%s";


    public static String getThum(int size){
        return String.format(THUMB_BASE,size,size);
    }



    /**
     *  获取简单上传的token
     */
    public  String getToken(){
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        String upToken = auth.uploadToken(BUCKET);
        logger.info("简单上传的token为:{}",upToken);
        return upToken;
    }

    /**
     *  获取覆盖上传的token
     */
    public  String getToken(String key){
        if(key==null){
            return getToken();
        }
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        String upToken = auth.uploadToken(BUCKET, key);
        logger.info("覆盖上传的key为:{},token为:{}",key,upToken);
        return upToken;
    }

    /**
     * 本地的文件上传
     */
    public String uploadFile(String localFilePath){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        UploadManager uploadManager = new UploadManager(cfg);
        //如果是Windows情况下，格式是 D:\\qiniu\\test.png
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        String upToken = getToken();
        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("上传文件成功,key:{},hash:{}",putRet.key,putRet.hash);
            return putRet.hash;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                logger.error("上传文件失败，{}",r.toString());
                logger.error(r.bodyString());
            } catch (QiniuException ex2) {
            }
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败");
        }
    }

    public   String getKey(String fileType){
        String key = UUID.randomUUID().toString().replace("-","")+"."+fileType;//上传资源的token
        return key;
    }
    /**
     * 文件流的文件上传
     */
    public DefaultPutRet uploadFile(InputStream inputStream, String key){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(zone);
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String upToken = getToken(key);
        try {
            Response response = uploadManager.put(inputStream,key,upToken,null, null);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            logger.info("上传文件成功,key:{},hash:{}",putRet.key,putRet.hash);
            return putRet;
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                logger.error("上传文件失败，{}",r.toString());
                logger.error(r.bodyString());
            } catch (QiniuException ex2) {
            }
            throw new BusinessException(ResultEnum.SERVER_ERROR,"上传文件失败,七牛云出现异常");
        }
    }

    /**
     * 删除文件
     */
    public void deleteFile(String key){
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());

        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(BUCKET ,key);
            logger.info("删除文件成功,key:{}",key);
        } catch (QiniuException ex) {
            logger.error("删除文件失败，code:{},msg:{}",ex.code(),ex.response.toString());
            throw new BusinessException(ResultEnum.SERVER_ERROR,"删除文件失败,七牛云出现异常");
        }
    }

    public String getDOMAIN() {
        return DOMAIN;
    }

    /**
     * 得到图片的链接
     * @param key
     * @return
     */
    public  String getUrl(String key){
        return DOMAIN+"/"+key+"?v="+System.currentTimeMillis();
    }

    /**
     * 得到各种大小图片的链接
     * @param key
     * @param modelSize 图片大小
     * @return
     */
    public  String getModelUrl(String key,Integer modelSize){
        return DOMAIN+"/"+key+"?v="+System.currentTimeMillis()+"&"+getThum(modelSize);
    }
}
