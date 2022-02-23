package com.wangjf.server.config.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: Jwt工具类
 * @author: Joker
 * @time: 2022/1/2 21:43
 */
@Component
public class JwtTokenUtil {
    private static final String CLAIM_KEY_USERNAME = "sub";//荷载中的用户名
    private static final String CLAIM_KEY_CREATED = "created";//JWT生成时间
    @Value("${jwt.secret}")
    private String secret;//JWT密钥
    @Value("${jwt.expiration}")
    private Long expiration;//JWT失效时间

    /**
     * @param userDetails
     * @description:根据用户信息生成JWT
     * @return: java.lang.String
     * @author: Joker
     * @time: 2022/1/3 15:23
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        //用户名利用spring-security获取
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * @param token
     * @description:从Token中获取登录用户名
     * @return: java.lang.String
     * @author: Joker
     * @time: 2022/1/3 15:34
     */
    public String getUserNameFromToken(String token) {
        String userName;
        try {
            Claims claims = getClaimsFromToken(token);//由于userName存放在Token的Claim中，所以需先从Token中获取Claim
            userName = claims.getSubject();
        } catch (Exception e) {
            userName = null;
        }
        return userName;
    }

    /**
     * @param token
     * @param userDetails
     * @description:判断Token是否有效
     * @return: boolean
     * @author: Joker
     * @time: 2022/1/3 15:54
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        //1.通过UserDetails获取的userName是否与Token负载中的userName一致
        //2.Token是否已经达到失效时间
        return userDetails.getUsername().equals(getUserNameFromToken(token)) && !isTokenExpired(token);
    }

    /**
     * @description:判断Token是否可以刷新(这边的刷新指的是改变失效时间)
     * @param token
            * @return: boolean
            * @author: Joker
            * @time: 2022/1/3 16:11
     */
    public boolean canRefresh(String token){
        return !isTokenExpired(token);
    }

    /**
     * @description:刷新Token
     * @param token
            * @return: java.lang.String
            * @author: Joker
            * @time: 2022/1/3 16:14
     */
    public String refreshToken(String token){
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * @description:判断Token是否已经到达失效时间
     * @param token
            * @return: boolean
            * @author: Joker
            * @time: 2022/1/3 15:59
     */
    private boolean isTokenExpired(String token) {
        Date date = getExpiredDateFromToken(token);
        return date.before(new Date(System.currentTimeMillis()));
    }

    /**
     * @description:通过Token获取过期时间
     * @param token
            * @return: java.util.Date
            * @author: Joker
            * @time: 2022/1/3 16:05
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * @param
     * @description:从Token中获取荷载Claim
     * @return: io.jsonwebtoken.Claims
     * @author: Joker
     * @time: 2022/1/3 15:43
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * @param claims
     * @description: 根据荷载生成JWT
     * @return: java.lang.String
     * @author: Joker
     * @time: 2022/1/3 15:25
     */
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * @param
     * @description:生成的Jwt的失效时间
     * @return: java.util.Date
     * @author: Joker
     * @time: 2022/1/3 15:30
     */
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}

