package org.xry.churchmodule.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class Jwt {
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()                                                                 //1*声明一个Jwt实例
                .withClaim("user", claims)                                            //一个自定义声明（键值对）,value存入的是一个hashmap，存储json数据
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 3))   //设置过期时间 当前时间+有有效时长
                .sign(Algorithm.HMAC256("Salt"));                                     //使用加密算法加入签名
    }

    public static Map<String, Object> parseToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("Salt")).build();      //1*使用密钥解码
        DecodedJWT decodedJWT =jwtVerifier.verify(token);                                    //验证token,失败会抛异常，成功会返回正常的解码对象
        return decodedJWT.getClaim("user").asMap();                 //获取源hashmap并返回
    }
}
