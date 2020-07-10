package com.insigma;

import java.util.Properties;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;

import tk.mybatis.spring.annotation.MapperScan;

import com.github.pagehelper.PageHelper;

@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.insigma.mapper")
@ServletComponentScan
public class VeteransplacementProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(VeteransplacementProjectApplication.class, args);
	}

	/**
	 * 查询结果分页配置
	 * @return
	 */
	@Bean
	public PageHelper pageHelper() {
		PageHelper pageHelper = new PageHelper();
		Properties properties = new Properties();
		properties.setProperty("offsetAsPageNum", "true");
		properties.setProperty("rowBoundsWithCount", "true");
		properties.setProperty("reasonable", "true");
		properties.setProperty("dialect", "mysql"); // 配置mysql数据库的方言
		pageHelper.setProperties(properties);
		return pageHelper;
	}
	/**  
     * 文件上传大小及数量配置  
     * 单个文件不超过10MB 最多不超过10个文件
     * @return  
     */  
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        DataSize dataSize= DataSize.ofMegabytes(500);//10MB
        //允许上传的文件最大值
        factory.setMaxFileSize(dataSize);
        /// 设置总上传数据总大小
        factory.setMaxRequestSize(dataSize);
        return factory.createMultipartConfig();
    }
    /**
     * 报表
     * @return
     */
//    @Bean 
//    public ServletRegistrationBean servletRegistrationBean() {
//    	return new ServletRegistrationBean(new ReportServlet(),"/ReportServer");
//    }
    
}
