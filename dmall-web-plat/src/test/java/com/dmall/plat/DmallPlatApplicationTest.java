package com.dmall.plat;

import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.sys.entity.Dict;
import com.dmall.sys.mapper.DictMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author:yuhang
 * @Date:2018/9/4
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DmallPlatApplicationTest {

    @Autowired
    private DictMapper dictMapper;

    @Test
    public void testDict(){
        Page page=new Page(1,10);
        List<Map> maps = dictMapper.dictPage(page, null, null);
        System.out.println(maps);
    }
}
