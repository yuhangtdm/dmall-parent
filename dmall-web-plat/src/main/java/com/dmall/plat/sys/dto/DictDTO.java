package com.dmall.plat.sys.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

/**
 * @author: yuhang
 * @date: 2018/9/3
 */
@Data
public class DictDTO {
    /**
     * 字典代码
     */
    private String dictType;
    /**
     * 字典名
     */
    private String dictName;

    /**
     * 代码
     */
    private String code;
    /**
     * 字典值
     */
    private String value;
}
