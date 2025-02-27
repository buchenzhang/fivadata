package com.example.fivedata.ex;

import com.alibaba.fastjson.JSON;
import com.example.fivedata.common.ResponseResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: Zing
 * @Date: 2024/10/15/17:43
 * @Description: 自定义认证失败处理器
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(),"用户名认证失败请重新登录");
        String json = JSON.toJSONString(result);
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().print(json);
    }
}
