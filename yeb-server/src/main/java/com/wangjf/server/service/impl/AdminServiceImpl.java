package com.wangjf.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangjf.server.config.jwt.JwtTokenUtil;
import com.wangjf.server.mapper.RoleMapper;
import com.wangjf.server.pojo.Admin;
import com.wangjf.server.mapper.AdminMapper;
import com.wangjf.server.pojo.Menu;
import com.wangjf.server.pojo.RespBean;
import com.wangjf.server.pojo.Role;
import com.wangjf.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SAAJResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author joker
 * @since 2022-01-02
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    @Lazy
    UserDetailsService userDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    AdminMapper adminMapper;
    @Autowired
    RoleMapper roleMapper;

    /**
     * @param userName
     * @param password
     * @param captcha
     * @param httpServletRequest
     * @description:登录成功返回Token，封装至公共返回对象RespBean中
     * @return: com.wangjf.server.pojo.RespBean
     * @author: Joker
     * @time: 2022/1/4 15:05
     */
    @Override
    public RespBean login(String userName, String password, String captcha, HttpServletRequest httpServletRequest) {
        String getCaptcha = (String) httpServletRequest.getSession().getAttribute("captcha");
        if (getCaptcha == null || !getCaptcha.equalsIgnoreCase(captcha)) {
            return RespBean.error("验证码输入错误！请重新输入");
        }
        //登录认证（通过前端传递过来的userName，从数据库中获取用户信息，封装到UserDetails对象中）
        /**
         *这边的loadUserByUsername()方法已在SecurityConfig类中重写
         */
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);//其实UserDetails就是Admin
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确");
        }
        //实际上，loadUserByUsername方法和matches方法可以直接调用以下代码，当然这得在配置类Security配置类中配置AuthenticationManager
        // UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //然后通过AdminLoginParam userDetails = (AdminLoginParam) authenticate.getPrincipal()获取到UserDetails，当然这个AdminLoginParam类得实现UserDetails接口
        //不过既然这边的Admin对象已经实现UserDetails接口，那么直接用它接收也行
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员");
        }

     /*   //更新security登录用户对象
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //userDetails就是Principal
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);*/

        //根据userDetails生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        //将token与tokenHeader封装至RespBean对象中
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHeader", tokenHeader);
        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * @param userName
     * @description:通过userName获取用户信息
     * @return: com.wangjf.server.pojo.Admin
     * @author: Joker
     * @time: 2022/1/6 15:58
     */
    @Override
    public Admin getAdminInfoByUserName(String userName) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", userName).eq("enabled", true));
    }

    /**
     * 根据用户id查询角色列表
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }
}
