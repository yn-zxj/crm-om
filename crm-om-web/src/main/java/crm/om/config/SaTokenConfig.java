package crm.om.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注解鉴权开启
 *
 * @author zhangxiaojun
 * @version 1.0
 */
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    /**
     * 注册Sa-Token拦截器
     *
     * @param registry 拦截器注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                // 允许对于网站静态资源的无授权访问(api文档地址)
                .excludePathPatterns("/favicon.ico", "/webjars/**", "/doc.html", "/v3/api-docs/**", "/error")
                // 允许匿名访问
                .excludePathPatterns("/auth/login", "/route/getConstantRoutes", "/bssApi/**", "/**");
    }
}
