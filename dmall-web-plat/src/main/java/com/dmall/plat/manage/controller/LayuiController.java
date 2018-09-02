package com.dmall.plat.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LayuiController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/brand")
    public String brand(){
        return "brand/list";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "welcome";
    }

    @RequestMapping("/test")
    public String test(){
        return "brand/test";
    }
}
