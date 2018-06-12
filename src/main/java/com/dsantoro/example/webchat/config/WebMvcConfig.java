package com.dsantoro.example.webchat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.dsantoro.example.webchat.controller"})
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	public ViewResolver internalResourceViewResolver() {

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");

		return viewResolver;

	}

	public void addViewControllers(ViewControllerRegistry registry) {

		registry
		.addViewController("/")
		.setViewName("chat");

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry
		.addResourceHandler("/resources/**")
		.addResourceLocations("/WEB-INF/resources/")
		.setCachePeriod(500);

	}

}
