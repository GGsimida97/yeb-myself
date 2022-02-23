package com.wangjf.server.mapper;

import com.wangjf.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wangjf.server.pojo.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * @description:通过用户id查询菜单列表
     * @param id
            * @return: java.util.List<com.wangjf.server.pojo.Menu>
            * @author: Joker
            * @time: 2022/1/9 13:31
     */

    List<Menu> selectMenusByAdminId(Integer id);
}
