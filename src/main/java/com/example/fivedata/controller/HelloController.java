package com.example.fivedata.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Zing
 * @Date: 2024/10/15/17:27
 * @Description: 基于springsecurity鉴权的hello接口
 */
@RestController
public class HelloController {


    @GetMapping("/hello")
    public String hello(){
        return "helloword";
    }
}
