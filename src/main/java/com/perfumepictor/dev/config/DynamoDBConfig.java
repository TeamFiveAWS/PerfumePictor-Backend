package com.perfumepictor.dev.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
@RequiredArgsConstructor
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(){
        return DynamoDbClient.builder().region(Region.AP_NORTHEAST_2)
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient){
        return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
    }
}
