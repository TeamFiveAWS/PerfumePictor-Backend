package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Feeds;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;


@Slf4j
@Repository
public class FeedRepository {

    private final DynamoDbTable<Feeds> feedsDynamoDbTable;

    public FeedRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.feedsDynamoDbTable = dynamoDbEnhancedClient.table("Feeds", TableSchema.fromBean(Feeds.class));
    }

    public void createFeed(Feeds feed){
        feedsDynamoDbTable.putItem(feed);
    }

}
