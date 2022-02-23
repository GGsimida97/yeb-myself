package com.wangjf.server.config.security;

import com.wangjf.server.config.jwt.JwtTokenUtil;
import com.wangjf.server.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description:Jwt【认证授权】过滤器
 * @author: Joker
 * @time: 2022/1/6 17:08
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    //Authorization : Bearer<token>....
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;//Authorization
    @Value("${jwt.tokenHead}")
    private String tokenHead;//Bearer
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        //这边就获取到 Bearer<token>....
        String authHeader = request.getHeader(tokenHeader);//通过key获取value
        if (null != authHeader && authHeader.startsWith(tokenHead)) {
            //取出token
            String authToken = authHeader.substring(tokenHead.length() + 1);

            //由token得到username
            String userName = jwtTokenUtil.getUserNameFromToken(authToken);
            //存在token但是没有登录(自己猜测是 直接访问资源的url并携带token，这样的话服务器端是没有 由这个token生成的Authentication的)
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //登录
                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
                //验证token是否有效,重新设置用户对象
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken
                            (userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

        }
        filterChain.doFilter(request, response);
    }

}
