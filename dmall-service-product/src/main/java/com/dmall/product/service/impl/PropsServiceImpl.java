package com.dmall.product.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.dmall.common.enums.ResultEnum;
import com.dmall.common.exception.BusinessException;
import com.dmall.common.utils.StringUtil;
import com.dmall.product.entity.Props;
import com.dmall.product.entity.PropsGroup;
import com.dmall.product.entity.PropsOption;
import com.dmall.product.mapper.PropsGroupMapper;
import com.dmall.product.mapper.PropsMapper;
import com.dmall.product.mapper.PropsOptionMapper;
import com.dmall.product.service.PropsOptionService;
import com.dmall.product.service.PropsService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dmall.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author yuhang
 * @since 2018-08-29
 */
@Service
public class PropsServiceImpl extends ServiceImpl<PropsMapper, Props> implements PropsService {

    @Autowired
    private PropsGroupMapper groupMapper;

    @Autowired
    private PropsOptionService propsOptionService;

    @Override
    public Page pageList(Props props, Page page) {
        EntityWrapper<Props> wrapper=new EntityWrapper<>();
        wrapper.orderBy("update_time",false);
        QueryUtil.queryForm(wrapper,props);
        page = this.selectPage(page,wrapper);
        return page;
    }

    @Override
    @Transactional
    public void saveOrUpdate(Props props,List<String> propValues) {
        PropsGroup group = groupMapper.selectById(props.getGroupId());
        if(group==null){
            throw new BusinessException(ResultEnum.SERVER_ERROR,"属性组不存在，请刷新后重试");
        }
        props.setProductType(group.getProductType());
        if (props.getId()!=null){
            this.updateById(props);
            List<PropsOption> optionList = propsOptionService.selectByPropId(props.getId());
            List<String> collect = optionList.stream().map(PropsOption::getOptionValue).collect(Collectors.toList());
            List<String> insert=new ArrayList<>();
            List<String> delete=new ArrayList<>();
            for (String propValue : propValues) {
                if(!collect.contains(propValue)){
                    insert.add(propValue);
                }
            }
            for (String s : collect) {
                if(!propValues.contains(s)) {
                    delete.add(s);
                }
            }
            if(StringUtil.isNotEmptyObj(insert)){
                batchInsert(props,insert);
            }
            if(StringUtil.isNotEmptyObj(delete)){
                propsOptionService.batchDelete(props.getId(),delete);
            }
        }else {
            this.insert(props);
            batchInsert(props,propValues);
        }
    }

    @Override
    public List<Props> listAll(String productTypeId, Long groupId) {
        EntityWrapper<Props> wrapper=new EntityWrapper<>();
        if(StringUtil.isNotBlank(productTypeId)){
            wrapper.eq("product_type",productTypeId);
        }

        if(groupId!=null){
            wrapper.eq("group_id",groupId);
        }
        return this.selectList(wrapper);
    }

    private void batchInsert(Props props,List<String> propValues){
        for(int i=0;i<propValues.size();i++){
            PropsOption propsOption=new PropsOption();
            propsOption.setPropsId(props.getId());
            propsOption.setOptionValue(propValues.get(i));
            propsOptionService.insert(propsOption);
        }
    }
}
