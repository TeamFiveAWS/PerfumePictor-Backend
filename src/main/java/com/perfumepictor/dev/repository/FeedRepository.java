package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Feed;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;


@Slf4j
@Repository
public class FeedRepository {

    @Value("${dynamodb.feed.table-name}")
    private String TABLE_NAME;
    private DynamoDbTable<Feed> feedsDynamoDbTable;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public FeedRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init(){
        feedsDynamoDbTable = dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Feed.class));
    }

    public void createFeed(Feed feed){
        feedsDynamoDbTable.putItem(feed);
    }

}
