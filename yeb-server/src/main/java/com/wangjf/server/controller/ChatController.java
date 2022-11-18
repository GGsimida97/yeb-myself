package com.wangjf.server.controller;

import com.wangjf.server.pojo.Admin;
import com.wangjf.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 在线聊天controller
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "获取所有操作员")
    public List<Admin> getAllAdmin(String keyWords) {
//        return adminService.getAllAdmins(keyWords);
        return null;
    }
}
