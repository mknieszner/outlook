package com.outlook.app.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

//  @Override
//  public void addViewControllers(ViewControllerRegistry registry) {
//    registry.addViewController("/base").setViewName("base");
//    registry.addViewController("/index").setViewName("index");
//    registry.addViewController("/mail").setViewName("mail");
//  }

//  @Override
//  public void addResourceHandlers(ResourceHandlerRegistry registry) {
//    registry
//        .addResourceHandler("/resources/**")
//        .addResourceLocations("/resources/");
//    //.setCachePeriod(31556926);
//  }

//  @Bean
//  public ThymeleafViewResolver thymeleafViewResolver(){
//    ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
//    return thymeleafViewResolver;
//  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
