package com.TrustUs.TrustUs_Bank.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.swing.*;

@Configuration
public class AppConfig {
    @Bean // Bean -> Creates global instance of an object which is going to exist throughout the lifecycle of application
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    @Bean
    public ModelMapper modelMapperConfig() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STANDARD);

        return modelMapper;
    }
}

/*

Bean 1: SpringTemplateEngine
This bean configures the Thymeleaf template engine, which is responsible for processing HTML templates and generating dynamic web pages.
The configuration specifies:
Prefix – the directory where HTML templates are located.
Suffix – the file extension (.html).
Character Encoding – UTF-8 to support all languages and special characters.
Template Resolver – tells Thymeleaf where and how to locate template files.


Bean 2: ModelMapper
This bean creates a ModelMapper object, which automatically maps data between two Java objects having similar fields.
It is commonly used to convert:
Entity → DTO
DTO → Entity
without manually copying each field.
The configuration:
Enables direct field matching.
Allows access to private fields.
Uses the STANDARD matching strategy for matching fields with similar names.


*/