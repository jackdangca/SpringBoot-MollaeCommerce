package com.molla.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        registry
                .addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");

        String categoryImagesDirName = "category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);

        String categoryImagePath = categoryImagesDir.toFile().getAbsolutePath();

        registry
                .addResourceHandler("/" + categoryImagesDirName + "/**")
                .addResourceLocations("file:/" + categoryImagePath + "/");


        String brandImagesDirName = "brand-logos";
        Path brandImagesDir = Paths.get(brandImagesDirName);

        String brandImagePath = brandImagesDir.toFile().getAbsolutePath();

        registry
                .addResourceHandler("/" + brandImagesDirName + "/**")
                .addResourceLocations("file:/" + brandImagePath + "/");

    }
}
