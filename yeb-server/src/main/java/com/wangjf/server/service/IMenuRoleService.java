package com.wangjf.server.service;

import com.wangjf.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjf.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单t_menu_role
     * @param rid
     * @param mids
     * @return
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
