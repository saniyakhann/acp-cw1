package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProcessService {

    @Value("${external.service.url}")
    private String baseUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> fetchAndProcess(String urlPath) throws Exception {
        // Fetch data from external service
        String fullUrl = urlPath != null ? urlPath : baseUrl + "/drones";
        String response = restTemplate.getForObject(fullUrl, String.class);

        // Parse JSON array
        JsonNode array = objectMapper.readTree(response);

        List<Map<String, Object>> processed = new ArrayList<>();
        for (JsonNode drone : array) {
            ObjectNode node = (ObjectNode) drone.deepCopy();
            JsonNode capability = node.get("capability");

            double costInitial = capability.has("costInitial") && !capability.get("costInitial").isNull()
                    ? capability.get("costInitial").asDouble() : 0.0;
            double costFinal = capability.has("costFinal") && !capability.get("costFinal").isNull()
                    ? capability.get("costFinal").asDouble() : 0.0;
            double costPerMove = capability.has("costPerMove") && !capability.get("costPerMove").isNull()
                    ? capability.get("costPerMove").asDouble() : 0.0;

            double costPer100Moves = costInitial + costFinal + costPerMove * 100;
            node.put("costPer100Moves", costPer100Moves);

            processed.add(objectMapper.convertValue(node, Map.class));
        }

        return processed;
    }
}
