package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Feed;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetResultPage;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchGetResultPageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch;


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

    public List<Feed> getFeeds(Set<String> keys) {
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        BatchGetItemEnhancedRequest batchGetItemRequest = BatchGetItemEnhancedRequest.builder()
                .readBatches(keys.stream()
                        .map(entry -> ReadBatch.builder(Feed.class)
                                .mappedTableResource(feedsDynamoDbTable)
                                .addGetItem(Key.builder()
                                        .partitionValue(entry.split("\\$")[0])
                                        .sortValue(entry.split("\\$")[1])
                                        .build())
                                .build())
                        .toList())
                .build();

        BatchGetResultPageIterable batchGetItemResponse = dynamoDbEnhancedClient.batchGetItem(batchGetItemRequest);

        List<Feed> resultFeeds = new ArrayList<>();
        for (BatchGetResultPage resultPage : batchGetItemResponse) {
            List<Feed> feeds = resultPage.resultsForTable(feedsDynamoDbTable);
            resultFeeds.addAll(feeds);
        }

        return resultFeeds;
    }

}
