package com.example.fivedata.filter;

import com.alibaba.fastjson.JSONObject;
import com.example.fivedata.common.JWTUtil;
import com.example.fivedata.security.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * @Author: Zing
 * @Date: 2024/10/15/17:43
 * @Description:
 */
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final RedisTemplate redisTemplate;


    /**
     * 返回null为token校验成功 失败返回错误信息
     * @param token
     * @return
     */
    public String checkToken(String token){
        if (token == null) {
            return "NOT LOGIN";
        }
        //校验token
        JWTUtil.validateToken(token);
        if (redisTemplate.opsForValue().get("token_" + JWTUtil.getId(token)) == null){
            return "TOKEN EXPIRED";
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug(request.getRequestURI());
        String token = request.getHeader("Authorization");
        if (token == null){
            filterChain.doFilter(request, response);
            return;
        }

        if (checkToken(token) != null){
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            filterChain.doFilter(request, response);
            return;
        }
        String redisKey = "login_" + JWTUtil.getId(token);
        Object obj = redisTemplate.opsForValue().get(redisKey);
        LoginUser loginUser = JSONObject.parseObject((String) obj, LoginUser.class);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }


        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
