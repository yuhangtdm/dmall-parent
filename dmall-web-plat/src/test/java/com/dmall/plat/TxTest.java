package com.dmall.plat;

import com.dmall.product.entity.PropsGroup;
import com.dmall.product.service.PropsGroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: yuhang
 * @date: 2018/11/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TxTest {

    @Autowired
    private PropsGroupService groupService;


    @Test
    public void txTest(){
        PropsGroup group = new PropsGroup();
        group.setId(12L);
        group.setName("时代大家");
        group.setProductType("153/172/165");
        groupService.txTest(group);
    }
}
