package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PostgresService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> getAllFromTable(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        return jdbcTemplate.queryForList(sql);
    }

    public void writeRows(String tableName, List<Map<String, Object>> rows) throws Exception {
        for (Map<String, Object> row : rows) {
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();
            List<Object> values = new ArrayList<>();

            int i = 0;
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                if (i > 0) {
                    columns.append(", ");
                    placeholders.append(", ");
                }
                columns.append("\"" + entry.getKey() + "\"");
                if (entry.getValue() instanceof Map) {
                    placeholders.append("?::JSONB");
                } else {
                    placeholders.append("?");
                }

                if (entry.getValue() instanceof Map) {
                    values.add(objectMapper.writeValueAsString(entry.getValue()));
                } else {
                    values.add(entry.getValue());
                }
                i++;
            }

            String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";
            jdbcTemplate.update(sql, values.toArray());
        }
    }
}
