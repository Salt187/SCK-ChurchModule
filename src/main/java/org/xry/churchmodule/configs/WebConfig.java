package org.xry.churchmodule.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xry.churchmodule.interceptors.LoginInterceptor;
import org.xry.churchmodule.interceptors.RefreshInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private RefreshInterceptor refreshInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截所有                                  //order越小优先级越高
        registry.addInterceptor(refreshInterceptor).order(0);

        //         设置放行接口
        registry.addInterceptor(loginInterceptor)
                .excludePathPatterns(
                        "/Admin/login").order(1);

    }

    //跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有接口生效
                // 允许的前端域名（生产环境建议指定具体域名，如"http://localhost:8080"）
//                .allowedOriginPatterns("http://localhost:5173")
                .allowedOriginPatterns("*")//允许全部访问

                // 允许的请求方法（GET、POST等，*表示所有）
                .allowedMethods("*")
                // 允许的请求头（必须包含Authorization，*表示所有）
                .allowedHeaders("*")
                // 允许前端读取的响应头（若后端需要返回自定义头，需在此声明）
                .exposedHeaders("Authorization")
                // 允许跨域请求携带cookie（若前端需要传递cookie，需设为true）
                .allowCredentials(false)//当 allowedOriginPatterns 为 * 时，allowCredentials 不能为 true
                // 预检请求的有效期（秒），避免频繁预检
                .maxAge(3600);
    }
}
