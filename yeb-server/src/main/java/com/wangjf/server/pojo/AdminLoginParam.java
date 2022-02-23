package com.wangjf.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @description:用户登录实体类
 * @author: Joker
 * @time: 2022/1/4 12:20
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)//默认为false，即在生成哈希值时忽略从父类继承过来的属性
@Accessors(chain = true)//默认为false，当该值为 true 时，对应字段的 setter 方法调用后，会返回当前对象。
@ApiModel(value = "AdminLoginParam对象",description = "")
public class AdminLoginParam {
    @ApiModelProperty(value = "用户名", required = true)
    private String userName;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @ApiModelProperty(value = "验证码", required = true)
    private String captcha;
}
