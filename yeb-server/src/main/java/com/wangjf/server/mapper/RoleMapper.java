package com.wangjf.server.mapper;

import com.wangjf.server.pojo.Role;
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
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id查询角色列表
     */
    List<Role> getRoles(Integer adminId);
}
