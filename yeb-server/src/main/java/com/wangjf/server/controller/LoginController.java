package com.wangjf.server.controller;

import com.wangjf.server.pojo.Admin;
import com.wangjf.server.pojo.AdminLoginParam;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @description:实现登录控制器
 * @author: Joker
 * @time: 2022/1/4 14:57
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回Token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest httpServletRequest) {

        return adminService.login(adminLoginParam.getUserName(), adminLoginParam.getPassword(), adminLoginParam.getCaptcha(),httpServletRequest);
    }

    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/login/info")
    public Admin getAdminInfo(Principal principal){
        if (principal == null) {
            return null;
        }
        String userName = principal.getName();
        Admin admin = adminService.getAdminInfoByUserName(userName);
        admin.setPassword(null);
        admin.setRoles(adminService.getRoles(admin.getId()));
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success("注销成功!");
    }
}
