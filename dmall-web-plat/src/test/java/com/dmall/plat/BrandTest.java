package com.dmall.plat;

import com.dmall.product.entity.Brand;
import com.dmall.product.service.BrandService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author: yuhang
 * @date: 2018/9/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BrandTest {
    @Autowired
    private BrandService brandService;

    @Test
    public void test1(){
        List<Brand> list = brandService.list();
        System.out.println(list);
    }
}
