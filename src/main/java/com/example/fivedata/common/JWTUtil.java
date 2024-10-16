package com.example.fivedata.common;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.fivedata.ex.CustomException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: Zing
 * @Date: 2024/10/15/17:43
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtil {
    //定义token返回头部
    public static String header;

    //签名密钥
    public static String secret;

    //存进客户端的token的key名
    public static final String TOKEN = "TOKEN";

    public void setHeader(String header) {
        JWTUtil.header = header;
    }

    public void setSecret(String secret) {
        JWTUtil.secret = secret;
    }

    /**
     * 创建TOKEN
     * @param
     * @return
     */
    public static String createToken(String id){
        return JWT.create()
                .withClaim("id",id)
                .sign(Algorithm.HMAC512(secret));
    }


    /**
     * 验证token
     * @param token
     */
    public static String validateToken(String token){
        try {
            return JWT.require(Algorithm.HMAC512(secret)).build().verify(token).toString();
        } catch (Exception e){
            throw new CustomException("token验证失败");
        }
    }


    /**
     * 获取载荷
     * @param token
     * @return
     */
    public static String getId(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(secret)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("id").asString();
    }


}
