package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

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

    public Optional<Like> getLike(Key key){
        return Optional.ofNullable(likesDynamoDbTable.getItem(key));
    }

    public void updateLike(Like like){
        likesDynamoDbTable.updateItem(like);
    }

}
