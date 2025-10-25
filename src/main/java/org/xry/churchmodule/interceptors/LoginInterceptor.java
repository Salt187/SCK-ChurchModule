package org.xry.churchmodule.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xry.churchmodule.utils.ThreadLocalUtils.UserId;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    //先进后出
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //先判断是否是 OPTIONS 预检请求，若是直接放行（无需验证 token）
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true; // 放行预检请求，让浏览器继续发真实请求
        }

        //验证threadLocal的存在
        if(UserId.getId()==null){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            // 设置响应体格式为 JSON
            response.setContentType("application/json;charset=UTF-8");
            // 写入具体错误信息
            response.getWriter().write("{\"code\": 401, \"message\": \"未授权访问，请先登录\"}");

            return false;
        }
        return true;
    }

}
