package com.yumistories.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * По умолчанию Spring Boot отдаёт статику только из resources/static
 * (то, что упаковано внутрь jar-а на этапе сборки). Папка uploads/
 * находится вне jar-а и пополняется во время работы приложения,
 * поэтому для неё нужен отдельный "мост" URL -> папка на диске.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String uploadDir;

    public WebConfig(@Value("${app.upload-dir}") String uploadDir) {
        this.uploadDir = uploadDir;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Запрос вида GET /photos/имя-файла.jpg будет искать файл
        // в папке uploadDir на диске.
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }
}