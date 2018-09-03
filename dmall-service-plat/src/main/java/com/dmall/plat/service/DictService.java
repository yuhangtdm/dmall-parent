package com.dmall.plat.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.sys.entity.Dict;
import com.baomidou.mybatisplus.service.IService;
import com.dmall.sys.entity.DictValue;

/**
 * <p>
 * 字典目录 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-09-03
 */
public interface DictService extends IService<Dict> {

    /**
     * 字典列表
     */
    Page pageList(Dict dict, DictValue dictValue, Page page);
}
