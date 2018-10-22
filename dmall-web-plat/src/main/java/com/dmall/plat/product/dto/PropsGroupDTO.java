package com.dmall.plat.product.dto;

import com.dmall.product.entity.Props;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 属性组dto
 * @author:yuhang
 * @Date:2018/9/17
 */
@Data
public class PropsGroupDTO implements Serializable {

    private Long id;

    @NotBlank(message = "属性组名称不可为空",groups = {SaveProps.class})
    private String name;

    @NotBlank(message = "商品类型不可为空",groups = {SaveProps.class})
    private String productType;

    @NotNull(message = "排序不可为空",groups = {SaveProps.class})
    private Integer sortIndex;

    /**
     * 简介
     */
    private String remark;

    /**
     * 是否是销售属性
     */
    private Integer isSale;

    private List<Props> propsList=new ArrayList<>();

    public interface SaveProps{}

}
