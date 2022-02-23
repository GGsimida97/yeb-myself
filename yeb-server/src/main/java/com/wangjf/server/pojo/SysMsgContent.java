package com.wangjf.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
@TableName("t_sys_msg_content")
@ApiModel(value="SysMsgContent对象", description="")
public class SysMsgContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "内容")
    private String message;

    @ApiModelProperty(value = "创建时间")
    @TableField("createDate")
    private LocalDateTime createdate;


}
