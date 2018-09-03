package com.dmall.plat.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.sys.entity.Dict;
import com.dmall.sys.entity.DictValue;
import com.dmall.sys.mapper.DictMapper;
import com.dmall.plat.service.DictService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典目录 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @Override
    public Page pageList(Dict dict, DictValue dictValue, Page page) {
        List<Map> data= dictMapper.dictPage(page,dict,dictValue);
        return page.setRecords(data);
    }
}
