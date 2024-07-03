package com.perfumepictor.dev.repository;

import com.perfumepictor.dev.entity.Profiles;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

@Repository
public class ProfileRepository {

    private final DynamoDbTable<Profiles> profilesDynamoDbTable;

    public ProfileRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.profilesDynamoDbTable = dynamoDbEnhancedClient.table("Profiles", TableSchema.fromBean(Profiles.class));
    }

    public void insertProfile(Profiles profiles){
        profilesDynamoDbTable.putItem(profiles);
    }

    public Optional<Profiles> findByEmail(String email){
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(email)
                        .build()
        );
        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(conditional)
                .limit(1)
                .build();
        return Optional.ofNullable(profilesDynamoDbTable.query(queryRequest).items().stream()
                .findAny()
                .orElse(null));
    }
}
