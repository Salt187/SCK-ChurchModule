package org.xry.churchmodule.utils.RedisUtils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.xry.churchmodule.exception.Exceptions.serviceException;
import org.xry.churchmodule.pojo.Code;
import org.xry.churchmodule.pojo.RedisConst;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class RedisCacheUtil {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    //延时
    public void expire(String key, Long time, TimeUnit timeUnit) {
        //修正time,增加随机值
        long finalTime =time + RandomUtil.randomLong((long) (time*0.1), (long) (time*0.2)+1);
        stringRedisTemplate.expire(key, finalTime, timeUnit);
    }


    //存入对象缓存
    public void set(String key, Object value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time, timeUnit);
    }

    //仅存入字符串缓存
    public void set(String key, String value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForValue().set(key, value, time, timeUnit);
    }

    //取出对象缓存
    public <R> R get(String key, Class<R> clazz) {
        return JSONUtil.toBean(stringRedisTemplate.opsForValue().get(key),clazz);
    }

    //仅取出字符串缓存
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    //尾存入列表一个对象，强制更新全体时间        理论上应该设置逻辑过期
    public void setList(String key,Object value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForList().rightPush(key,JSONUtil.toJsonStr(value));
        this.expire(key,time, timeUnit);
    }

    //尾存入列表一个字符串
    public void setList(String key, String value, Long time, TimeUnit timeUnit) {
        stringRedisTemplate.opsForList().rightPush(key,value);
        this.expire(key,time, timeUnit);
    }

    //取出列表,明确内部存入的为字符串
    public List<String> getList(String key){
        return stringRedisTemplate.opsForList().range(key,0,-1);
    }

    //删除键值对
    public void delete(String key){
        stringRedisTemplate.delete(key);
    }

    //非热点key的通用查询并更新的方案，不用互斥锁    key    缓存类型     数据库的查询方法
    public <R,T> R queryCache(String preFixKey,T key,Class<R> type, Function<T,R> dbFallBack, Long time, TimeUnit timeUnit){
        //R表示预期的返回值类型   T表示查询条件的类型      数据库只能依据T查询，而redis需要依据T拼接的字符串查询

        //合成Redis类型的key
        String fixedKey = preFixKey+key;

        //先从redis中查缓存
        String JsonStr = stringRedisTemplate.opsForValue().get(fixedKey);

        //判断是否为空
        if(StrUtil.isNotBlank(JsonStr)){
            try {
                //查询键的剩余过期时间，小于总时长的 1/3 时才重置
                Long remainingTime = stringRedisTemplate.getExpire(fixedKey, timeUnit);
                if (remainingTime < time / 3) {
                    System.out.println("缓存剩余时间不足，已重置过期时间");
                    stringRedisTemplate.expire(fixedKey, time, timeUnit);
                } else {
                    System.out.println("成功获取缓存，剩余时间充足，无需重置");
                }
                return JSONUtil.toBean(JsonStr, type);
            } catch (JSONException e) {
                System.out.println("缓存反序列化失败，key：" + fixedKey + "，异常：" + e.getMessage());
                // 反序列化失败时，清除无效缓存（避免后续请求继续命中无效值）
                stringRedisTemplate.delete(fixedKey);
            }
        }

        //检查是否为防御值       (可能使缓存穿透的防御机制)
        if(RedisConst.NULL.equals(JsonStr)){
            System.out.println("命中预防缓存穿透的空值，重置防御时间");
            this.expire(fixedKey, RedisConst.MSG_EXPIRE, TimeUnit.MINUTES);
            //抛缓存穿透异常
            throw new serviceException("缓存穿透防御值命中", Code.BUSINESS_ERROR);
        }

        //请求数据库
        R result = dbFallBack.apply(key);

        if(result == null){
            System.out.println("缓存和数据库均为null，触发缓存穿透防御");
            this.set(fixedKey,RedisConst.NULL,5L,TimeUnit.MINUTES);
            throw new serviceException("服务器无相关信息",Code.SELECT_ERROR);
        }
        //存缓存
        this.set(fixedKey,JSONUtil.toJsonStr(result),time,timeUnit);

        //返回数据
        return result;
    }


    //存入逻辑过期的数据 自动封装逻辑日期                    保质期
    public void setWithLogical(String key, Object value,Long time, TimeUnit timeUnit) {
        //设置逻辑过期对象
        RedisLogicalData data = new RedisLogicalData(value, LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        //写入Redis
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(data));
    }

    //逻辑过期的查询/重建 自动检查过期，自动到数据库更新
    public <R,T> R logicalExpireCache(String preFixKey, T key, Class<R> type, Function<T,R> dbFallBack, Long time, TimeUnit timeUnit){
        //R表示预期的返回值类型   T表示查询条件的类型      数据库只能依据T查询，而redis需要依据T拼接的字符串查询

        //合成Redis类型的key
        String fixedKey = preFixKey+key;

        //先从redis中查缓存
        String JsonStr = stringRedisTemplate.opsForValue().get(fixedKey);
        //反序列化取对象
        RedisLogicalData rld = JSONUtil.toBean(JsonStr, RedisLogicalData.class);

        //检查是否为防御值      (可能使缓存穿透的防御机制)
        if(JsonStr.equals(RedisConst.NULL)){
            System.out.println("命中预防缓存穿透的空值");
            throw new serviceException("触发缓存穿透防御",Code.BUSINESS_ERROR);
        }

        //非防御值，若未逻辑过期
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time));
        if(expireTime.isAfter(LocalDateTime.now())){
            //非null也未过期
            return JSONUtil.toBean(JsonStr,type);
        }

        //无缓存且非空值防御，尝试获取互斥锁重建数据
        System.out.println("尝试获取互斥锁");
        boolean flag =tryLock("Look:"+key);
        R result;

        try {
            //未取到锁，直接返回旧数据
            if(!flag){
                return JSONUtil.toBean(JsonStr,type);
            }

            //双重检查，避免缓存更新成功但又有线程取到了锁
            LocalDateTime expireTime2 = LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time));
            if(expireTime2.isAfter(LocalDateTime.now())){
                //非null也未过期
                return JSONUtil.toBean(JsonStr,type);
            }


            //确实是需要更新的操作
            System.out.println("抢锁成功，即将更新缓存");

            //请求数据库
            result = dbFallBack.apply(key);

            //存缓存
            this.set(fixedKey,JSONUtil.toJsonStr(result),time,timeUnit);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            //释放锁
            unlock("Look:"+key);
        }

        //返回数据
        return result;
    }


    //互斥锁
    private boolean tryLock(String key){
        //                                                                          预设锁的时长，大于缓存操作时间
        boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        //需要进行拆箱操作
        return BooleanUtil.isTrue(flag);
    }
    private void unlock(String key){
        stringRedisTemplate.delete(key);
    }
}
