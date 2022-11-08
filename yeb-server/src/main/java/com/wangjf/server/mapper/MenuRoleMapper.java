package com.wangjf.server.mapper;

import com.wangjf.server.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 更新角色菜单（批量插入）
     * @param rid
     * @param mids
     */
    Integer insertRecords(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
