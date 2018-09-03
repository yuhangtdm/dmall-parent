package com.dmall.plat;

import com.dmall.product.entity.Brand;
import com.dmall.product.service.BrandService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DmallWebPlatApplicationTests {

    @Autowired
    private BrandService brandService;

    @Test
    public void contextLoads() {
        List<Brand> brands = brandService.list();
        System.out.println(brands);
    }

    @Test
    public void update(){
        Brand brand=new Brand();
        brand.setBrandName("华为");
        brand.setEnglishName("HUAWEI");
        brand.setDescription("华为水军无敌");
        brand.setFirstLetter("H");
        List<Brand> brands = brandService.saveOrUpdate(brand);
        System.out.println(brands);

    }

}
