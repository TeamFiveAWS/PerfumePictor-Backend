package com.perfumepictor.dev.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Profiles {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String nickname;

    @NotNull
    private String imageUrl;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("email")
    public String getEmail() {
        return email;
    }

    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("nickname")
    public String getNickname() {
        return nickname;
    }

    @DynamoDbAttribute("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

}
