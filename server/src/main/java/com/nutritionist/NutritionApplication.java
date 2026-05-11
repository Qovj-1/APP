package com.nutritionist;

import com.nutritionist.config.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class NutritionApplication {

  public static void main(String[] args) {
    SpringApplication.run(NutritionApplication.class, args);
  }
}
