package com.example.demo.controller;

import com.example.demo.service.DynamoDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/acp")
public class DynamoDBController {

    @Autowired
    private DynamoDBService dynamoDBService;

    @GetMapping("/all/dynamo/{table}")
    public ResponseEntity<?> getAllFromTable(@PathVariable String table) {
        try {
            List<Map<String, Object>> results = dynamoDBService.getAllItems(table);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/single/dynamo/{table}/{key}")
    public ResponseEntity<?> getSingleItem(@PathVariable String table,
                                           @PathVariable String key) {
        try {
            Map<String, Object> result = dynamoDBService.getSingleItem(table, key);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}