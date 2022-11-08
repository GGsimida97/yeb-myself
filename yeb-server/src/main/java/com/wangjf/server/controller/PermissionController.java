package com.wangjf.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangjf.server.pojo.Menu;
import com.wangjf.server.pojo.MenuRole;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.pojo.Role;
import com.wangjf.server.service.IMenuRoleService;
import com.wangjf.server.service.IMenuService;
import com.wangjf.server.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限组 角色管理
 */
@RestController
@RequestMapping("/system/basic/permiss")
public class PermissionController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMenuRoleService menuRoleService;

    @ApiOperation("获取所有角色")
    @GetMapping("/")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation("添加一个角色")
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role) {
        if (!role.getName().startsWith("ROLE_")) {
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

    @ApiOperation("查询所有菜单")
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @ApiOperation("根据角色id查询菜单id")
    @GetMapping("/mid/{rid}")
    public List<Integer> getMidByRid(@PathVariable Integer rid) {
        return menuRoleService.list(new QueryWrapper<MenuRole>().eq("rid", rid)).stream().map(MenuRole::getMid).collect(Collectors.toList());
    }

    @ApiOperation("更新角色菜单")
    @PutMapping("/")
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        return menuRoleService.updateMenuRole(rid, mids);
    }
}
