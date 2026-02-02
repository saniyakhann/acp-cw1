package com.example.demo.controller;

import com.example.demo.service.PostgresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/acp")
public class PostgresController {

    @Autowired
    private PostgresService postgresService;

    @GetMapping("/all/postgres/{table}")
    public ResponseEntity<?> getAllFromTable(@PathVariable String table) {
        try {
            List<Map<String, Object>> results = postgresService.getAllFromTable(table);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}