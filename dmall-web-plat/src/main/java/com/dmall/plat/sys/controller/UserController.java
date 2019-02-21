package com.dmall.plat.sys.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.annotation.TransBean;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.plat.sys.dto.DictDTO;
import com.dmall.plat.sys.dto.UserDTO;
import com.dmall.plat.sys.service.DictService;
import com.dmall.plat.sys.service.UserService;
import com.dmall.sys.entity.Dict;
import com.dmall.sys.entity.User;
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
 * 用户管理 前端控制器
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 数据字典分页
     */
    @RequestMapping("list")
    @ResponseBody
    @TransBean
    public ReturnResult list(User user, Page page){
        page=userService.pageList(user,page);
        return ResultUtil.buildSuccess(page.getTotal(),page.getRecords());
    }

    /**
     * 跳转到字典编辑页面
     */
    @RequestMapping("edit")
    public String edit(Long id, HttpServletRequest request){
        if(id!=null){
            User user = userService.selectById(id);
            if(user==null){
                throw new BusinessException(ResultEnum.BAD_REQUEST,"该用户不存在,请刷新列表");
            }
            request.setAttribute("bean", user);
        }
        return "/sys/user/edit";
    }

    /**
     * 保存或更新用户
     */
    @RequestMapping("save")
    @ResponseBody
    public ReturnResult save(@Validated UserDTO userDTO){
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);
        userService.saveOrUpdate(user);
        return ResultUtil.buildResult(ResultEnum.SUCC);
    }


}

