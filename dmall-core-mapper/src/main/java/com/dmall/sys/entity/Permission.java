package com.dmall.sys.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yuhang
 * @since 2019-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 资源类型
     */
    private Integer type;
    /**
     * 父级id
     */
    @TableField("parent_id")
    private Long parentId;
    /**
     * 资源路径
     */
    private String url;
    /**
     * 资源图标
     */
    private String icon;
    /**
     * 菜单是否打开 0-关闭 1-打开
     */
    private Integer open;
    /**
     * 权限字符串
     */
    private String permission;
    /**
     * 是否可用
     */
    private Integer status;
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;


}
