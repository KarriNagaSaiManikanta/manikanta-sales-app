package com.example.manikantasales.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Upload folder absolute path
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

        // ==========================
        // DEBUG
        // ==========================
        System.out.println("=====================================");
        System.out.println("UPLOAD DIRECTORY : " + uploadDir);
        System.out.println("UPLOAD EXISTS    : " + uploadDir.toFile().exists());

        Path banner = uploadDir.resolve("banners/mobile-banner.jpg");

        System.out.println("BANNER PATH      : " + banner);
        System.out.println("BANNER EXISTS    : " + banner.toFile().exists());
        System.out.println("=====================================");

        // Uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir.toString() + "/");

        // CSS
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // JS
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // Images
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }
}