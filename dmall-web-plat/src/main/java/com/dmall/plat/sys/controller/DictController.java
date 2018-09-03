package com.dmall.plat.sys.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.plat.service.DictService;
import com.dmall.plat.sys.dto.DictDTO;
import com.dmall.product.entity.Brand;
import com.dmall.sys.entity.Dict;
import com.dmall.sys.entity.DictValue;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 字典目录 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
@Controller
@RequestMapping("/sys/dict")
public class DictController {
    @Autowired
    private DictService dictService;

    @RequestMapping("list")
    @ResponseBody
    @TransBean
    public ReturnResult list(Dict dict, DictValue dictValue, Page page){
        page=dictService.pageList(dict,dictValue,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }
}

