package studio.lineage2.club.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import studio.lineage2.club.interceptor.MainInterceptor;
import studio.lineage2.club.interceptor.RecaptchaInterceptor;

import java.io.File;

/**
 Obi-Wan
 27.05.2016
 */
@Configuration @PropertySource(value = "file:./application.properties", ignoreResourceNotFound = true) @PropertySource(value = "file:./public/application.properties", ignoreResourceNotFound = true) @EnableWebMvc public class WebConfig extends WebMvcConfigurerAdapter
{
	@Autowired private RecaptchaInterceptor recaptchaInterceptor;
	@Autowired private MainInterceptor mainInterceptor;

	@Override public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		String currentPath = new File(".").getAbsolutePath();
		String location = "file:///" + currentPath + "/public/";
		registry.addResourceHandler("/**").addResourceLocations(location);
	}

	@Override public void addInterceptors(InterceptorRegistry registry)
	{
		registry.addInterceptor(recaptchaInterceptor);
		registry.addInterceptor(mainInterceptor);
	}
}