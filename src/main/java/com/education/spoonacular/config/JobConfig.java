package com.education.spoonacular.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
@ConfigurationProperties(prefix = "spoonacular")
@RequiredArgsConstructor
public class JobConfig {
    private final Map<String, JobProperties> jobs = new HashMap<>();

}