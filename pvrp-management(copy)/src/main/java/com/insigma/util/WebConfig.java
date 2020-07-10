package com.insigma.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.insigma.webtool.interceptor.LoginInterceptor;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private LoginInterceptor loginInterceptor;

	
	@Value("${rootpath}")
    private String rootPath;
	@Override    
    public void addResourceHandlers(ResourceHandlerRegistry registry) {  
        registry.addResourceHandler("/file/**").addResourceLocations("file:"+rootPath);  
    }  
	
	// 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
	  @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	    // addPathPatterns("/**") 表示拦截所有的请求，
	    // excludePathPatterns("/login", "/register") 表示除了登陆与注册之外，因为登陆注册不需要登陆也可以访问
	    registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns("/doLogin","/loginOut","/sys/function/queryFunctionList");
	  }


}
