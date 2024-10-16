package com.example.fivedata;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.fivedata.entity.User;
import com.example.fivedata.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class FivedataApplicationTests {


    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
    }

    @Test
    void db(){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName,"test");
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }


}
