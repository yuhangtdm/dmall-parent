package com.dmall.plat;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.product.entity.Brand;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.mapper.PropsGroupMapper;
import com.dmall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author: yuhang
 * @date: 2018/9/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandTest {
    @Autowired
    private BrandService brandService;

    @Autowired
    private PropsGroupMapper propsGroupMapper;

    @Test
    public void test1(){
        List<Brand> list = brandService.list();
        System.out.println(list);
    }

    @Test
    public void test2(){
        Page page=new Page(1,10);
        PropsGroup group=new PropsGroup();
        List<Map<String, Object>> maps = propsGroupMapper.pageList(page, group);
        System.out.println(maps);


    }
}
