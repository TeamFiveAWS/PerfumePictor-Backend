package com.perfumepictor.dev.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedConfig {

    @Value("${dynamodb.feed.pk}")
    private String defaultPk;

    public static String DEFAULT_PK;

    @PostConstruct
    public void init() {
        DEFAULT_PK = defaultPk;
    }

}
