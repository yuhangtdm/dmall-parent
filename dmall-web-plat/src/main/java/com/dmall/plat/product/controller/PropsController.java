package com.dmall.plat.product.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.plat.product.dto.PropsDTO;
import com.dmall.product.entity.Props;
import com.dmall.product.service.PropsService;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 商品属性 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-08-25
 */
@Controller
@RequestMapping("props")
public class PropsController {

    @Autowired
    private PropsService propsService;

    /**
     *属性列表
     */
    @RequestMapping("list")
    @ResponseBody
    @TransBean
    public ReturnResult list(Props props, Page page){
        page=propsService.pageList(props,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 跳转到属性编辑页面
     */
    @RequestMapping("edit")
    public String edit(Long id, HttpServletRequest request){
        if(id!=null){
            Props props = propsService.selectById(id);
            if(props==null){
                throw new BusinessException(ResultEnum.BAD_REQUEST,"该属性不存在,请刷新列表");
            }
            request.setAttribute("bean",props);
        }
        return "product/props/edit";
    }

    /**
     * 保存或更新属性
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated PropsDTO propsDTO){
        Props props=new Props();
        BeanUtils.copyProperties(propsDTO,props);
        propsService.saveOrUpdate(props);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 删除属性
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnResult delete(@NotNull(message = "id不能为空") Long id){
        propsService.deleteById(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }
}

