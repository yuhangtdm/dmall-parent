package com.dmall.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.product.entity.PropsOption;
import com.dmall.product.mapper.PropsOptionMapper;
import com.dmall.product.service.PropsOptionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 属性选项 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class PropsOptionServiceImpl extends ServiceImpl<PropsOptionMapper, PropsOption> implements PropsOptionService {

    @Autowired
    private PropsOptionMapper mapper;

    @Override
    public List<PropsOption> selectByPropId(Long id) {
        EntityWrapper<PropsOption> wrapper=new EntityWrapper<>();
        wrapper.eq("props_id",id);
        return this.selectList(wrapper);
    }

    @Override
    public void batchDelete(Long id, List<String> delete) {
        EntityWrapper<PropsOption> wrapper=new EntityWrapper<>();
        wrapper.eq("props_id",id);
        wrapper.in("option_value",delete);
        this.delete(wrapper);
    }

    @Override
    public List<JSONObject> queryOptionsByPropsId(Long propsId) {
        return  mapper.queryOptionsByPropsId(propsId);
    }
}
