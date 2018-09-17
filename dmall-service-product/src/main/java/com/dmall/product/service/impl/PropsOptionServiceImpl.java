package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.dmall.product.entity.PropsOption;
import com.dmall.product.mapper.PropsOptionMapper;
import com.dmall.product.service.PropsOptionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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

    @Override
    public List<PropsOption> selectByPropId(Long id) {
        EntityWrapper<PropsOption> wrapper=new EntityWrapper<>();
        wrapper.eq("property_id",id);
        return this.selectList(wrapper);
    }

    @Override
    public void batchDelete(Long id, List<String> delete) {
        EntityWrapper<PropsOption> wrapper=new EntityWrapper<>();
        wrapper.eq("property_id",id);
        wrapper.in("optionValue",delete);
        this.delete(wrapper);
    }
}
