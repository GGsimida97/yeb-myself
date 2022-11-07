package com.wangjf.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "url")
    private String url;

    @ApiModelProperty(value = "path")
    private String path;

    @ApiModelProperty(value = "组件")
    private String component;

    @ApiModelProperty(value = "菜单名")
    private String name;

    @ApiModelProperty(value = "图标")
    @TableField("iconCls")
    private String iconcls;

    @ApiModelProperty(value = "是否保持激活")
    @TableField("keepAlive")
    private Boolean keepalive;

    @ApiModelProperty(value = "是否要求权限")
    @TableField("requireAuth")
    private Boolean requireauth;

    @ApiModelProperty(value = "父id")
    @TableField("parentId")
    private Integer parentid;

    @ApiModelProperty(value = "是否启用")
    private Boolean enabled;

    @ApiModelProperty(value = "子菜单")
    @TableField(exist = false)//表明表字段中没有children
    private List<Menu> children;

    @ApiModelProperty(value = "角色列表")
    @TableField(exist = false)
    private List<Role> roles;
}
