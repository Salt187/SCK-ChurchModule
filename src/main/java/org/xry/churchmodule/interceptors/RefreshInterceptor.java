package org.xry.churchmodule.interceptors;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xry.churchmodule.exception.Exceptions.tokenValidationException;
import org.xry.churchmodule.pojo.RedisConst;
import org.xry.churchmodule.utils.Jwt;
import org.xry.churchmodule.utils.RedisUtils.RedisCacheUtil;
import org.xry.churchmodule.utils.ThreadLocalUtils.UserId;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RefreshInterceptor implements HandlerInterceptor {

    @Resource
    private RedisCacheUtil redisCacheUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws tokenValidationException {
        //先判断是否是 OPTIONS 预检请求，若是直接放行（无需验证 token）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 放行预检请求，让浏览器继续发真实请求
        }

        String token = request.getHeader("Authorization");      //1*获取请求头中的token
        if(token == null)return true;

        //解析令牌并获取核心id
        Map<String, Object> claims  = Jwt.parseToken(token);
        Integer id =(Integer) claims.get("id");

        //验证Redis中的token
        List<String> tokens = redisCacheUtil.getList(RedisConst.TOKEN + id);

        if(tokens.contains(token)){
            UserId.setId(id);
        } else {
            return true;
        }

        redisCacheUtil.expire(RedisConst.TOKEN +id,RedisConst.TOKEN_EXPIRE, TimeUnit.MINUTES);
        return true;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
        UserId.remove();                                   //2*清理ThreadLocal的数据
    }
}
