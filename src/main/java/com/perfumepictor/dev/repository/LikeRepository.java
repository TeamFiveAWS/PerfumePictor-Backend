package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
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
import software.amazon.awssdk.enhanced.dynamodb.model.ReadBatch;

@Slf4j
@Repository
public class LikeRepository {

    @Value("${dynamodb.like.table-name}")
    private String TABLE_NAME;
    private DynamoDbTable<Like> likesDynamoDbTable;
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;

    public LikeRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init(){
        likesDynamoDbTable = dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(Like.class));
    }

    public Optional<Like> getLike(String userId, String feedKey){
        return Optional.ofNullable(likesDynamoDbTable.getItem(Key.builder()
                .partitionValue(userId)
                .sortValue(feedKey)
                .build()));
    }

    public List<Like> getLikes(String userId, Set<String> keys){
        if (keys.isEmpty()) {
            return new ArrayList<>();
        }

        BatchGetItemEnhancedRequest batchGetItemRequest = BatchGetItemEnhancedRequest.builder()
                .readBatches(keys.stream()
                        .map(key -> ReadBatch.builder(Like.class)
                                .mappedTableResource(likesDynamoDbTable)
                                .addGetItem(Key.builder()
                                        .partitionValue(userId)
                                        .sortValue(key)
                                        .build())
                                .build())
                        .toList())
                .build();

        BatchGetResultPageIterable batchGetItemResponse = dynamoDbEnhancedClient.batchGetItem(batchGetItemRequest);

        List<Like> resultLikes = new ArrayList<>();
        for (BatchGetResultPage resultPage : batchGetItemResponse) {
            List<Like> likes = resultPage.resultsForTable(likesDynamoDbTable);
            resultLikes.addAll(likes);
        }

        return resultLikes;
    }

    public void updateLike(Like like){
        likesDynamoDbTable.updateItem(like);
    }

}
