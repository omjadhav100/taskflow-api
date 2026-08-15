package com.taskflow.taskflow_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
           .allowedOrigins(
                        "http://localhost:5173",
                        "https://taskflow-frontend-xxxx.onrender.com" // replace with your real deployed frontend URL
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");    
    }
}
