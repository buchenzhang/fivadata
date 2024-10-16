package com.example.fivedata.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fivedata.common.JWTUtil;
import com.example.fivedata.common.ResponseResult;
import com.example.fivedata.entity.User;
import com.example.fivedata.mapper.UserMapper;
import com.example.fivedata.security.LoginUser;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @Author: Zing
 * @Date: 2024/10/15/17:43
 * @Description:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user, HttpServletResponse response){
        // 因为是样例直接写在controller了
        try{
            UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken
                    (user.getUserName(),user.getPassword());
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            if (Objects.isNull(authenticate)){
                throw new RuntimeException("登录失败");
            }

            LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

            String userid = loginUser.getUser().getId().toString();

            String jwt = JWTUtil.createToken(userid);
            response.setHeader("Authorization",jwt);

            redisTemplate.opsForValue().set("login_"+userid, JSONObject.toJSONString(loginUser));
            redisTemplate.opsForValue().set("token_" + userid,jwt,1, TimeUnit.DAYS);

            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUserName,user.getUserName());
            User res = userMapper.selectOne(wrapper);
            res.setPassword(null);
            return new ResponseResult(200,"登录成功",res);
        }catch (BadCredentialsException e){
            return new ResponseResult<>(300,e.getMessage());
        }
    }




}
