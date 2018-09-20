package com.dmall.plat.sys.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.JsonUtil;
import com.dmall.plat.product.dto.BrandDTO;
import com.dmall.plat.sys.dto.DictDTO;
import com.dmall.plat.sys.service.DictService;
import com.dmall.sys.entity.Dict;
import com.dmall.web.common.result.ReturnResult;
import com.dmall.web.common.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
@Controller
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 数据字典分页
     */
    @RequestMapping("list")
    @ResponseBody
    public ReturnResult list(Dict dict, Page page){
        page=dictService.pageList(dict,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 跳转到字典编辑页面
     */
    @RequestMapping("edit")
    public String edit(Long id, HttpServletRequest request){
        if(id!=null){
            Dict dict = dictService.selectById(id);
            if(dict==null){
                throw new BusinessException(ResultEnum.BAD_REQUEST,"该数据字典不存在,请刷新列表");
            }
            request.setAttribute("bean", JsonUtil.toJson(dict));
        }
        return "/sys/dict/edit";
    }

    /**
     * 保存或更新字典
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated DictDTO dictDTO){
        Dict dict=new Dict();
        BeanUtils.copyProperties(dictDTO,dict);
        dictService.saveOrUpdate(dict);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 启用字典
     */
    @RequestMapping("active")
    @ResponseBody
    public ReturnResult delete(@NotNull(message = "id不能为空") Long id){
        dictService.active(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 停用字典
     */
    @RequestMapping("invalid")
    @ResponseBody
    public ReturnResult invalid(@NotNull(message = "id不能为空") Long id){
        dictService.invalid(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

}

