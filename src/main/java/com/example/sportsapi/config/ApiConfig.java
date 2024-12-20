package com.example.sportsapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "api.sports")
public class ApiConfig {
    @Value("${api.sports.base-url}")
    private String baseUrl;

    @Value("${api.sports.key}")
    private String apiKey;

    @Value("${spring.gcs.bucket.name}")
    private String bucketName;


    // leagueIds from documentation
    List<Integer> leagueIds = Arrays.asList(2, 3, 39, 45, 61, 78, 88, 94, 135, 140, 145, 203);

    public String getBaseUrl() {
        return baseUrl;
    }

    public List<Integer> getleagueIds() {
        return leagueIds;
    }

    public String getBucketName() {
        return bucketName;
    }
    public String getApiKey() {
        return apiKey;
    }

}
