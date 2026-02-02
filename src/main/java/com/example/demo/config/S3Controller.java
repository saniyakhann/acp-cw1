package com.example.demo.controller;

import com.example.demo.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acp")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/all/s3/{bucket}")
    public ResponseEntity<?> getAllFromBucket(@PathVariable String bucket) {
        try {
            List<Object> results = s3Service.getAllObjects(bucket);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/single/s3/{bucket}/{key}")
    public ResponseEntity<?> getSingleObject(@PathVariable String bucket,
                                             @PathVariable String key) {
        try {
            Object result = s3Service.getSingleObject(bucket, key);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
