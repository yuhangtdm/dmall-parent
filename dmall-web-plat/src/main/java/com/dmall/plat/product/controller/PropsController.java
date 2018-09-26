package com.dmall.plat.product.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.Constants;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.plat.product.dto.PropsDTO;
import com.dmall.plat.product.dto.PropsGroupDTO;
import com.dmall.product.entity.ProductType;
import com.dmall.product.entity.Props;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.entity.PropsOption;
import com.dmall.product.service.ProductTypeService;
import com.dmall.product.service.PropsGroupService;
import com.dmall.product.service.PropsOptionService;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
@Validated
public class PropsController {

    @Autowired
    private PropsService propsService;

    @Autowired
    private ProductTypeService productTypeService;

    @Autowired
    private PropsGroupService propsGroupService;

    @Autowired
    private PropsOptionService propsOptionService;

    /**
     *属性组列表
     */
    @RequestMapping("group/list")
    @ResponseBody
    public ReturnResult groupList(PropsGroup group, Page page){
        page=propsGroupService.pageList(group,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 属性组编辑页面
     */
    @RequestMapping("group/edit")
    public String groupEdit(Long id, HttpServletRequest request){
        if(id!=null){
            PropsGroup group = propsGroupService.selectById(id);
            if(group==null){
                throw new BusinessException(ResultEnum.BAD_REQUEST,"该属性组不存在,请刷新列表");
            }
            request.setAttribute("bean",group);
        }
        return "commodity/props/group_edit";
    }

    /**
     * 属性组保存
     */
    @RequestMapping("group/save")
    @ResponseBody
    public ReturnResult groupSave(@Validated PropsGroupDTO groupDTO){
        validGroup(groupDTO);
        PropsGroup group=new PropsGroup();
        BeanUtils.copyProperties(groupDTO,group);
        propsGroupService.saveOrUpdate(group);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     * 前端校验 校验商品类型
     */
    private void validGroup(PropsGroupDTO groupDTO) {
        ProductType type =null;
        try {
            String productType=groupDTO.getProductType();
            Long productTypeId=Long.parseLong(productType.substring(productType.lastIndexOf("/")+1));
            type=productTypeService.selectById(productTypeId);
        }catch (Exception e){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"商品类型productType格式不正确");
        }
        if(type==null){
            throw new BusinessException(ResultEnum.BAD_REQUEST,"商品类型已被删除，请重新选择");
        }
    }

    /**
     * 属性组设置属性页面
     */
    @RequestMapping("group/setProp")
    public String setProp(@NotNull(message = "属性组id不能为空") Long groupId, HttpServletRequest request){
        return "commodity/props/setProp";
    }

    /**
     * 属性编辑页面
     */
    @RequestMapping("edit")
    public String edit(Long id, HttpServletRequest request){
        if(id!=null){
            Props props = propsService.selectById(id);
            if(props==null){
                throw new BusinessException(ResultEnum.BAD_REQUEST,"该属性不存在,请刷新列表");
            }
            List<PropsOption> optionList=propsOptionService.selectByPropId(id);
            List<String> collect = optionList.stream().map(PropsOption::getOptionValue).collect(Collectors.toList());
            String values = StringUtil.join(collect,",");
            request.setAttribute("values",values);
            request.setAttribute("bean",props);
        }
        return "commodity/props/edit";
    }

    /**
     * 属性组列表
     * @param productTypeId
     * @return
     */
    @RequestMapping("group/listAll")
    @ResponseBody
    public ReturnResult listAll(String productTypeId){
        List<PropsGroup> result=propsGroupService.listAll(productTypeId);
        return ResultUtil.buildResult(ResultEnum.SUCC,result);
    }


    /**
     * 属性保存 包含属性值
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated PropsDTO propsDTO){
        Props group=new Props();
        BeanUtils.copyProperties(propsDTO,group);
        propsService.saveOrUpdate(group,propsDTO.getPropValues());
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    /**
     *属性分页列表
     */
    @RequestMapping("list")
    @ResponseBody
    @TransBean
    public ReturnResult list(Props props, Page page){
        page=propsService.pageList(props,page);
        return ResultUtil.buildResult(ResultEnum.SUCC,page.getTotal(),page.getRecords());
    }

    /**
     * 删除属性
     */
    @RequestMapping("delete")
    @ResponseBody
    public ReturnResult delete(@NotNull(message = "id不能为空") Long id){
        //todo 属性组维护了商品 不可删除 属性维护了商品 不可删除
        //propsService.deleteById(id);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }

    @RequestMapping("listAll")
    @ResponseBody
    public ReturnResult propsListAll(String productTypeId,Long groupId){
        List<Props> result=propsService.listAll(productTypeId,groupId);
        return ResultUtil.buildResult(ResultEnum.SUCC,result);
    }

}

