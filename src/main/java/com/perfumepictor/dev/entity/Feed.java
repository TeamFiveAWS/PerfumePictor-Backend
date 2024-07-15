package com.perfumepictor.dev.entity;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Random;
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
public class Feed {

    @NotNull
    private String PK;

    @NotNull
    private String SK;

    @NotNull
    private String userId;

    @NotNull
    private String profileImg;

    @NotNull
    private String content;

    @NotNull
    private String contentImg;

    @NotNull
    private String perfumeInfo;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime updatedAt;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("PK")
    public String getPK() {
        return PK;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("SK")
    public String getSK() {
        return SK;
    }

    @DynamoDbAttribute("userId")
    public String getUserId() {
        return userId;
    }

    @DynamoDbAttribute("profileImg")
    public String getProfileImg() {
        return profileImg;
    }

    @DynamoDbAttribute("content")
    public String getContent() {
        return content;
    }

    @DynamoDbAttribute("contentImg")
    public String getContentImg() {
        return contentImg;
    }

    @DynamoDbAttribute("perfumeInfo")
    public String getPerfumeInfo() {
        return perfumeInfo;
    }

    @DynamoDbAttribute("createdAt")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute("updatedAt")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getKey() {
        return this.PK + "$" + this.SK;
    }

    public static String getPKFromKey(String key) {
        return key.split("\\$")[0];
    }

    public static String getSKFromKey(String key) {
        return key.split("\\$")[1];
    }

    public static FeedBuilder builder() {
        return new CustomFeedBuilder();
    }

    private static class CustomFeedBuilder extends FeedBuilder {

        @Override
        public Feed build() {
            if (super.PK == null) {
                super.PK = super.userId;
            }
            if (super.createdAt == null) {
                super.createdAt = LocalDateTime.now();
            }
            if (super.updatedAt == null) {
                super.updatedAt = LocalDateTime.now();
            }
            if (super.SK == null) {
                super.SK = super.createdAt.toString() + "#" + generateRandomNumber(4);
            }
            return super.build();
        }

        private String generateRandomNumber(int length) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                sb.append(random.nextInt(10));
            }
            return sb.toString();
        }
    }

    // TODO: updatedAt 자동 변경..
}
