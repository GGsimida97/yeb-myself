package com.wangjf.server.controller;


import com.wangjf.server.pojo.Menu;
import com.wangjf.server.service.IAdminService;
import com.wangjf.server.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@RestController
@RequestMapping("/system/cfg")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "通过用户id获取菜单列表")//用户id不靠前端传递（安全问题），通过spring-security中的全局作用域对象获取
    //((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId(){
        return menuService.getMenusByAdminId();
    }
}
