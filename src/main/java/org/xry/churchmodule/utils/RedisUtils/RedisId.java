package org.xry.churchmodule.utils.RedisUtils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


@Component
public class RedisId {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //前缀业务功能key
    public long nextId(String preKey){
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSeconds = now.toEpochSecond(ZoneOffset.UTC);
        long timeStamp = nowSeconds - 1640995200L;

        //生成序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));

        //自增长
        long count = stringRedisTemplate.opsForValue().increment(preKey+":"+date);

        return timeStamp << 32 | count;

    }

}
