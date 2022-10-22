package com.wangjf.server.service.impl;

import com.wangjf.server.pojo.Admin;
import com.wangjf.server.pojo.Menu;
import com.wangjf.server.mapper.MenuMapper;
import com.wangjf.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId = ((Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 先从redis获取菜单数据
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + adminId);
        if (menus == null) {
            menus = menuMapper.getMenusByAdminId(adminId);
            valueOperations.set("menu_" + adminId, menus);
        }
        return menus;
    }
}
