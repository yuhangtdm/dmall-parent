package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.utils.ChineseCharToEnUtil;
import com.dmall.product.entity.Brand;
import com.dmall.product.mapper.BrandMapper;
import com.dmall.product.service.BrandService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-31
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

    @Override
    public Page pageList(Brand brand, Page page) {
        EntityWrapper<Brand> wrapper=new EntityWrapper<>();
        wrapper.orderBy("create_time");
        QueryUtil.queryForm(wrapper,brand);
        page = this.selectPage(page,wrapper);
        return page;
    }

    @Override
    public void saveOrUpdate(Brand brand) {
        if (brand.getId()!=null){
            brand.setFirstLetter(ChineseCharToEnUtil.getFirstLetter(brand.getBrandName()));
            this.updateById(brand);
        }else {
            brand.setFirstLetter(ChineseCharToEnUtil.getFirstLetter(brand.getBrandName()));
            this.insert(brand);
        }
    }
}
