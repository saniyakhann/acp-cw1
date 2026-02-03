package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Object> getAllObjects(String bucket) throws IOException {
        List<Object> results = new ArrayList<>();

        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucket)
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        for (S3Object s3Object : listResponse.contents()) {
            String key = s3Object.key();
            Object content = getSingleObject(bucket, key);
            results.add(content);
        }

        return results;
    }

    public Object getSingleObject(String bucket, String key) throws IOException {
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getRequest);
        return objectMapper.readValue(response, Object.class);
    }

    public void writeObjects(List<Map<String, Object>> items) throws IOException {
        for (Map<String, Object> item : items) {
            String name = item.get("name").toString();
            String json = objectMapper.writeValueAsString(item);

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket("s2147128")
                    .key(name)
                    .contentType("application/json")
                    .build();

            s3Client.putObject(putRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(json.getBytes()));
        }
    }

    public void writeObjectsWithUUID(List<Map<String, Object>> items) throws IOException {
        for (Map<String, Object> item : items) {
            String key = java.util.UUID.randomUUID().toString();
            String json = objectMapper.writeValueAsString(item);

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket("s2147128")
                    .key(key)
                    .contentType("application/json")
                    .build();

            s3Client.putObject(putRequest, software.amazon.awssdk.core.sync.RequestBody.fromBytes(json.getBytes()));
        }
    }
}