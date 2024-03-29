package com.wangjf.server.service;

import com.baomidou.mybatisplus.extension.api.R;
import com.wangjf.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wangjf.server.pojo.Menu;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
public interface IAdminService extends IService<Admin> {
    /**
     * @param userName
     * @param password
     * @param captcha
     * @param httpServletRequest
     * @description:登录成功返回Token，封装至公共返回对象RespBean中
     * @return: com.wangjf.server.pojo.RespBean
     * @author: Joker
     * @time: 2022/1/4 15:06
     */
    RespBean login(String userName, String password, String captcha, HttpServletRequest httpServletRequest);


    /**
     * @param userName
     * @description:通过uerName获取用户信息
     * @return: com.wangjf.server.pojo.Admin
     * @author: Joker
     * @time: 2022/1/6 15:58
     */
    Admin getAdminInfoByUserName(String userName);

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);
}
