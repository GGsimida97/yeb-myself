package com.wangjf.server.mapper;

import com.wangjf.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 根据用户id查询Menus
     * @param id
     * @return
     */
    List<Menu> getMenusByAdminId(Integer id);

    /**
     * 根据角色获取菜单列表
     * @return
     */
    List<Menu> getMenusWithRole();

    /**
     * 查询所有菜单
     */
    List<Menu> getAllMenus();
}
