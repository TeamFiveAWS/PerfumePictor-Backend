package com.perfumepictor.dev.config.dynanoDB;

import com.perfumepictor.dev.utils.Status;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;


// TODO: To Be Deleted
public class StatusEnumTypeConverter implements AttributeConverter<Status> {

    @Override
    public AttributeValue transformFrom(Status status) {
        return AttributeValue.builder().s(status.name()).build();
    }

    @Override
    public Status transformTo(AttributeValue value) {
        return Status.valueOf(value.s());
    }

    @Override
    public EnhancedType<Status> type() {
        return null;
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
