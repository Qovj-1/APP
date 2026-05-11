package com.nutritionist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("*")
        .allowedHeaders("*")
        .exposedHeaders("Authorization");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String uploadDir = new File("uploads/avatars").getAbsolutePath();
    registry
        .addResourceHandler("/api/uploads/avatars/**")
        .addResourceLocations("file:" + uploadDir + File.separator);
  }
}
