package com.dmall.plat.sys.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: yuhang
 * @date: 2018/9/3
 */
@Data
public class DictDTO implements Serializable {

    private Long id;

    /**
     * 字典类型代码
     */
    @NotBlank(message = "字典类型代码不能为空")
    private String dictType;
    /**
     * 字典名
     */
    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    /**
     * 代码
     */
    @NotBlank(message = "字典代码不能为空")
    private String dictCode;
    /**
     * 字典值
     */
    @NotBlank(message = "字典值不能为空")
    private String dictValue;

    /**
     * 是否有效
     */
    @NotNull(message = "是否有效不能为空")
    private Integer status;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空")
    private Integer sortIndex;

}
