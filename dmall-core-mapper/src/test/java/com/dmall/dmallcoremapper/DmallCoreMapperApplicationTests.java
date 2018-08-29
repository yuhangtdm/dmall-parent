package com.dmall.dmallcoremapper;

import com.dmall.product.entity.ProductType;
import com.dmall.product.mapper.ProductTypeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DmallCoreMapperApplicationTests {

    @Autowired
    ProductTypeMapper productTypeMapper;

    @Test
    public void contextLoads() {
        ProductType productType=new ProductType();
        productType.setName("手机");
        productType.setLevel(1);
        productType.setDescription("这是手机");
        productTypeMapper.insert(productType);
        System.out.println(productType.getId());
    }

}
