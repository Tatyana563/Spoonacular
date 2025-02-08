package com.education.spoonacular.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@Setter
@Getter
@Configuration
@EnableConfigurationProperties(SpoonProperties.class)
public class SpoonConfig {
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
