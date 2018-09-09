package com.dmall.plat.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LayuiController {

    /**
     * 首页
     */
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    /**
     * 品牌
     */
    @RequestMapping("/brand")
    public String brand(){
        return "product/brand/list";
    }

    /**
     * 欢迎页
     */
    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 数据字典
     */
    @RequestMapping("/dict")
    public String dict(){
        return "sys/dict/list";
    }

    /**
     * 商品类型
     */
    @RequestMapping("/type")
    public String productType(){
        return "product/type/list";
    }

    /**
     * 属性
     */
    @RequestMapping("/props")
    public String props(){
        return "product/props/list";
    }
}
