package com.dmall.product.service;

import com.dmall.product.entity.PropsOption;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 属性选项 服务类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
public interface PropsOptionService extends IService<PropsOption> {

    List<PropsOption> selectByPropId(Long id);

    void batchDelete(Long id, List<String> delete);
}
