package com.example.demo.controller;

import com.example.demo.model.UrlRequest;
import com.example.demo.service.ProcessService;
import com.example.demo.service.DynamoDBService;
import com.example.demo.service.S3Service;
import com.example.demo.service.PostgresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/acp")
public class ProcessController {

    @Autowired
    private ProcessService processService;
    @Autowired
    private DynamoDBService dynamoDBService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private PostgresService postgresService;

    @PostMapping("/process/dump")
    public ResponseEntity<?> processDump(@RequestBody UrlRequest request) {
        try {
            List<Map<String, Object>> results = processService.fetchAndProcess(request.getUrlPath());
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/process/dynamo")
    public ResponseEntity<?> processDynamo(@RequestBody UrlRequest request) {
        try {
            List<Map<String, Object>> results = processService.fetchAndProcess(request.getUrlPath());
            dynamoDBService.writeItems(results);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/process/s3")
    public ResponseEntity<?> processS3(@RequestBody UrlRequest request) {
        try {
            List<Map<String, Object>> results = processService.fetchAndProcess(request.getUrlPath());
            s3Service.writeObjects(results);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/process/postgres/{table}")
    public ResponseEntity<?> processPostgres(@PathVariable String table, @RequestBody UrlRequest request) {
        try {
            List<Map<String, Object>> results = processService.fetchAndProcess(request.getUrlPath());
            postgresService.writeRows(table, results);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}