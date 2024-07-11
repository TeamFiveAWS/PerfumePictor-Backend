package com.perfumepictor.dev.entity;

import com.perfumepictor.dev.utils.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDateTime;

@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Feeds {

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    private String imageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Status status;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute("nickname")
    public String getNickname() {
        return nickname;
    }

    @DynamoDbAttribute("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @DynamoDbAttribute("created_at")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @DynamoDbAttribute("updated_at")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @DynamoDbAttribute("status")
    public Status getStatus() {
        return status;
    }
}
