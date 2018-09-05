package com.dmall.plat.sys.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.dmall.sys.entity.Dict;

import java.util.List;

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
     * 按条件查询分页
     * @param dict
     * @param page
     * @return
     */
    Page pageList(Dict dict, Page page);

    /**
     * 根据字典类型获取字典列表
     * @param dictType
     * @return
     */
    public List<Dict> queryDictByType(String dictType);

    /**
     * 保存或更新字典
     * @param dict
     */
    List<Dict>  saveOrUpdate(Dict dict);

    /**
     * 根据id删除字典
     */
    List<Dict>  deleteById(Long id);
}
