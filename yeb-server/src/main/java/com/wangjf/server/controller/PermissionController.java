package com.wangjf.server.controller;

import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.pojo.Role;
import com.wangjf.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限组 角色管理
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {
    @Autowired
    private IRoleService roleService;

    @ApiOperation("获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation("添加一个角色")
    @PostMapping("/")
    public RespBean addRole(@RequestBody Role role) {
        if (! role.getName().startsWith("ROLE_")) {
            role.setName("ROLE_" + role.getName());
        }
        if (roleService.save(role)) {
            return RespBean.success("添加角色成功");
        }
        return RespBean.error("添加角色失败");
    }

    @ApiOperation("删除一个角色")
    @DeleteMapping("/{rid}")
    public RespBean deleteRole(@PathVariable Integer rid) {
        if (roleService.removeById(rid)) {
            return RespBean.success("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
