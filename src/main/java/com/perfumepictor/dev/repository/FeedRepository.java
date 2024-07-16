package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Feed;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


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

    public Optional<Feed> getFeed(String feedKey) {
        return Optional.ofNullable(feedsDynamoDbTable.getItem(Key.builder()
                .partitionValue(Feed.getPKFromKey(feedKey))
                .sortValue(Feed.getSKFromKey(feedKey))
                .build()));
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
                                        .partitionValue(Feed.getPKFromKey(entry))
                                        .sortValue(Feed.getSKFromKey(entry))
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

    public Map<String, Object> getFeedsByUserId(String userId, String lastEvaluatedKey, int pageSize) {

        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder().partitionValue(userId).build());

        QueryEnhancedRequest.Builder queryEnhancedRequestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .limit(pageSize)
                .scanIndexForward(false);

        if (lastEvaluatedKey != null && !lastEvaluatedKey.isEmpty()) {
            String[] keyParts = lastEvaluatedKey.split("\\$");
            Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
            exclusiveStartKey.put("PK", AttributeValue.builder().s(keyParts[0]).build());
            exclusiveStartKey.put("SK", AttributeValue.builder().s(keyParts[1]).build());
            queryEnhancedRequestBuilder.exclusiveStartKey(exclusiveStartKey);
        }

        QueryEnhancedRequest queryEnhancedRequest = queryEnhancedRequestBuilder.build();

        PageIterable<Feed> pagedResult = feedsDynamoDbTable.query(queryEnhancedRequest);

        List<Feed> feedList = new ArrayList<>();
        Map<String, AttributeValue> nextKey = null;

        Page<Feed> firstPage = pagedResult.stream().findFirst().orElse(null);
        if (firstPage != null) {
            feedList.addAll(firstPage.items());
            nextKey = firstPage.lastEvaluatedKey();
        }

        String lastFeedKey = null;
        if (nextKey != null) {
            lastFeedKey = nextKey.get("PK").s() + "$" + nextKey.get("SK").s();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("feeds", feedList);
        result.put("lastFeedKey", lastFeedKey);

        return result;
    }

    public void updateFeed(Feed feed) {
        feedsDynamoDbTable.updateItem(feed);
    }

    public void deleteFeed(String feedKey) {
        feedsDynamoDbTable.deleteItem(Key.builder()
                .partitionValue(Feed.getPKFromKey(feedKey))
                .sortValue(Feed.getSKFromKey(feedKey))
                .build());
    }

}
