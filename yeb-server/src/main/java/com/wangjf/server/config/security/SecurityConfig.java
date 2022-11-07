package com.wangjf.server.config.security;

import com.wangjf.server.config.security.component.*;
import com.wangjf.server.pojo.Admin;
import com.wangjf.server.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @description:spring-security基本配置
 * @author: Joker
 * @time: 2022/1/6 16:35
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    @Lazy
    private AdminServiceImpl adminService;
    @Autowired
    private RestAuthenticationEntryPoint authenticationEntryPoint;//未登录
    @Autowired
    private RestfulAccessDeniedHandler accessDeniedHandler;//未授权

    @Autowired
    private CustomFilter customFilter;
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    /**
     * @param web
     * @description:放行一些url(安全拦截机制)
     * @return: void
     * @author: Joker
     * @time: 2022/1/8 14:19
     */
    @Override

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login",
                "/logout",
                "/ws/**",
                "/css/**",
                "/js/**",
                "/index.html",
                "favicon.ico",//网站的一些图标
                "/doc.html",
                "/webjars/**",
                "/swagger-resources/**",
                "/v2/api-docs/**",
                "/captcha");
    }

    /**
     * @param http
     * @description:spring-security认证授权的相关设置
     * @return: void
     * @author: Joker
     * @time: 2022/1/6 16:57
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //使用JWT,不需要开启csrf
        // 跨站请求伪造（英语：Cross-site request forgery），也被称为 one-click attack 或者 session riding，
        // 通常缩写为 CSRF 或者 XSRF， 是一种挟制用户在当前已登录的Web应用程序上执行非本意的操作的攻击方法
        http.csrf()
                .disable()
                //使用Token，不需要session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                /*   //不拦截的请求
                   .antMatchers("/login", "/logout")
                   .permitAll()
                   //除了上面，其他请求都得进行拦截认证*/
                .anyRequest()
                .authenticated()
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(customUrlDecisionManager);
                        o.setSecurityMetadataSource(customFilter);
                        return o;
                    }
                })
                .and()
                //禁用缓存
                .headers()
                .cacheControl();

        //添加自定义的JWT登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint);


    }


    /**
     * @param auth
     * @description:执行自定义的认证登录（就是重写了原本AuthenticationManagerBuilder类调用UserDetailsService接口的loadUserByUsername方法 以及使得AuthenticationManagerBuilder类调用PasswordEncoder接口的BCryptPasswordEncoder实现类）
     * @return: void
     * @author: Joker
     * @time: 2022/1/6 16:48
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }


    /**
     * @param
     * @description:重写loadUserByUsername()
     * @return: org.springframework.security.core.userdetails.UserDetailsService
     * @author: Joker
     * @time: 2022/1/6 16:37
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminService.getAdminInfoByUserName(username);
            if (admin != null) {
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    /**
     * @param
     * @description:将PasswordEncoder注入到容器
     * @return: org.springframework.security.crypto.password.PasswordEncoder
     * @author: Joker
     * @time: 2022/1/6 16:41
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }
}
