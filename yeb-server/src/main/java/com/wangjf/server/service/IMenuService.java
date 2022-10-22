package com.wangjf.server.service;

import com.wangjf.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface IMenuService extends IService<Menu> {
    List<Menu> getMenusByAdminId();
}
