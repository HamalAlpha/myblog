package top.arieslee.myblog.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName WebMvcConfig
 * @Description 自定义MVC插件
 * @Author Aries
 * @Date 2018/7/12 16:26
 * @Version 1.0
 **/
@Component
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private BaseInterceptor baseInterceptor;

    @Override
    //加入自定义拦截器
    public void addInterceptors(InterceptorRegistry registry) {
        //加入拦截器，可进一步设置拦截哪些请求和排除拦截哪些请求
        super.addInterceptors(registry);
        registry.addInterceptor(baseInterceptor).addPathPatterns("/**");
                //.excludePathPatterns("");
    }
}
