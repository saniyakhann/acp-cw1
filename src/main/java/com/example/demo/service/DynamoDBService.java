package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;

@Service
public class DynamoDBService {

    @Autowired
    private DynamoDbClient dynamoDbClient;

    public List<Map<String, Object>> getAllItems(String tableName) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse response = dynamoDbClient.scan(scanRequest);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            results.add(convertItem(item));
        }

        return results;
    }

    public Map<String, Object> getSingleItem(String tableName, String key) {
        GetItemRequest getRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of("id", AttributeValue.builder().s(key).build()))
                .build();

        GetItemResponse response = dynamoDbClient.getItem(getRequest);

        if (!response.hasItem()) {
            throw new RuntimeException("Item not found");
        }

        return convertItem(response.item());
    }

    private Map<String, Object> convertItem(Map<String, AttributeValue> item) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
            result.put(entry.getKey(), convertAttributeValue(entry.getValue()));
        }
        return result;
    }

    private Object convertAttributeValue(AttributeValue value) {
        if (value.s() != null) return value.s();
        if (value.n() != null) return Double.parseDouble(value.n());
        if (value.bool() != null) return value.bool();
        return null;
    }
}
