package com.wangjf.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangjf.server.pojo.MenuRole;
import com.wangjf.server.mapper.MenuRoleMapper;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {
    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单t_menu_role（这边的更新包括 删除、新增、修改）
     *
     * @param rid
     * @param mids
     * @return
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        // 先将对应rid的所有mid删除
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if (null == mids || mids.length == 0) {
            return RespBean.success("更新成功");
        }
        Integer result = menuRoleMapper.insertRecords(rid, mids);
        if (result == mids.length) {
            return RespBean.success("更新成功");
        }
        return RespBean.error("更新失败");
    }
}
