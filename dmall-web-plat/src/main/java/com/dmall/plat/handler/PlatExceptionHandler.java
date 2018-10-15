package com.dmall.plat.handler;


import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import org.springframework.beans.BeansException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataAccessException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.*;

/**
 * 异常处理器
 * 有自适应效果 即浏览器访问时返回页面
 * postman请求时返回json数据
 * @author: yuhang
 * @date: 2018/9/1
 */
@ControllerAdvice
public class PlatExceptionHandler  {

    /**
     * mvc运行过程中遇到的自定义异常
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public String businessHandle(BusinessException ex, HttpServletRequest request){
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("code",ex.getErrorCode());
        map.put("msg",ex.getMsg());
        request.setAttribute("javax.servlet.error.status_code",ex.getErrorCode());
        request.setAttribute("data",map);
        return "forward:/error";
    }

    /**
     * 参数运行遇到的异常
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(TypeMismatchException.class)
    public String businessHandle(TypeMismatchException ex, HttpServletRequest request){
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("code",ResultEnum.BAD_REQUEST.getCode());
        map.put("msg","参数格式传递错误,参数:"+ex.getPropertyName()+",类型应该是："+ex.getRequiredType());
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.BAD_REQUEST.getCode());
        request.setAttribute("data",map);
        return "forward:/error";
    }

    /**
     * 用于对实体的校验 普通提交的方式
     * @param e
     * @return
     */
    @ExceptionHandler(BindException.class)
    public String bind(BindException e, HttpServletRequest request){
        Map<String,Object> map=new LinkedHashMap<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuffer sb=new StringBuffer();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage()).append("\n");
        }
        map.put("code",ResultEnum.BAD_REQUEST.getCode());
        map.put("msg",sb.toString());
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.BAD_REQUEST.getCode());
        request.setAttribute("data",map);
        return "forward:/error";
    }

    /**
     * 对方法参数的校验 获取错误的校验信息
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public String bind(ConstraintViolationException e, HttpServletRequest request){
        Map<String,Object> map=new LinkedHashMap<>();
        StringBuffer sb=new StringBuffer();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : constraintViolations) {
            sb.append(constraintViolation.getMessage());
        }
        map.put("code",ResultEnum.BAD_REQUEST.getCode());
        map.put("msg",sb.toString());
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.BAD_REQUEST.getCode());
        request.setAttribute("data",map);

        return  "forward:/error";
    }

    /**
     * 针对于 @RequestBody注解的实体可以获得相应参数
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String bind(MethodArgumentNotValidException e, HttpServletRequest request){
        Map<String,Object> map=new LinkedHashMap<>();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        StringBuffer sb=new StringBuffer();
        for (FieldError fieldError : fieldErrors) {
            sb.append(fieldError.getDefaultMessage());
        }
        map.put("code",ResultEnum.BAD_REQUEST.getCode());
        map.put("msg",sb.toString());
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.BAD_REQUEST.getCode());
        request.setAttribute("data",map);

        return  "forward:/error";
    }


    @ExceptionHandler(BeansException.class)
    public String bind(BeansException e, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        map.put("code",ResultEnum.SERVER_ERROR.getCode());
        map.put("msg","对象属性拷贝失败");
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.SERVER_ERROR.getCode());
        request.setAttribute("data",map);
        return  "forward:/error";
    }

    @ExceptionHandler(DataAccessException.class)
    public String sqlException(DataAccessException ex, HttpServletRequest request){
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("code",ResultEnum.SERVER_ERROR.getCode());
        map.put("msg","数据库异常");
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.SERVER_ERROR.getCode());
        request.setAttribute("data",map);
        return "forward:/error";
    }

    @ExceptionHandler(Exception.class)
    public String exception(Exception ex, HttpServletRequest request){
        ex.printStackTrace();
        Map<String,Object> map=new LinkedHashMap<>();
        map.put("code",ResultEnum.SERVER_ERROR.getCode());
        map.put("msg","未知异常");
        request.setAttribute("javax.servlet.error.status_code",ResultEnum.SERVER_ERROR.getCode());
        request.setAttribute("data",map);
        return "forward:/error";
    }


}
