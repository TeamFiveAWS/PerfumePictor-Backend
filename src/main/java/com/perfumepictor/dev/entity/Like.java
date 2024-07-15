package com.perfumepictor.dev.entity;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Like {

    @NotNull
    private String userId;

    @NotNull
    private String feedKey;

    @NotNull
    boolean like;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("userId")
    public String getUserId() {
        return userId;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("feedKey")
    public String getFeedKey() {
        return feedKey;
    }

    @DynamoDbAttribute("like")
    public boolean isLike() {
        return like;
    }

    @DynamoDbAttribute("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void toggleLike() {
        this.like = !this.like;
    }

    public static LikeBuilder builder() {
        return new Like.CustomLikeBuilder();
    }

    private static class CustomLikeBuilder extends LikeBuilder {

        @Override
        public Like build() {
            if (super.createdAt == null) {
                super.createdAt = LocalDateTime.now();
            }
            if (super.updatedAt == null) {
                super.updatedAt = LocalDateTime.now();
            }
            return super.build();
        }
    }

}
