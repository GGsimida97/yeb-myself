package com.wangjf.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_mail_log")
@ApiModel(value="MailLog对象", description="")
public class MailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息id")
    @TableId("msgId")
    private String msgid;

    @ApiModelProperty(value = "接收员工id")
    private Integer eid;

//    @ApiModelProperty(value = "状态（0:消息投递中 1:投递成功 2:投递失败）")
//    private Integer msgStatus;
    @ApiModelProperty(value = "状态（1:消息投递到交换机成功 2：1:消息投递到交换机失败）")
    private Integer exchangeStatus;
    @ApiModelProperty(value = "状态（1:消息成功路由到队列 2：消息路由到队列失败）")
    private Integer routingStatus;
//    @ApiModelProperty(value = "状态（1:邮件发送成功 2：邮件发送失败）")
//    private Integer mailStatus;

    @ApiModelProperty(value = "路由键")
    @TableField("routeKey")
    private String routekey;

    @ApiModelProperty(value = "交换机")
    private String exchange;

    @ApiModelProperty(value = "重试次数")
    private Integer count;

    @ApiModelProperty(value = "重试时间")
    @TableField("tryTime")
    private LocalDateTime trytime;

    @ApiModelProperty(value = "创建时间")
    @TableField("createTime")
    private LocalDateTime createtime;

    @ApiModelProperty(value = "更新时间")
    @TableField("updateTime")
    private LocalDateTime updatetime;


}
