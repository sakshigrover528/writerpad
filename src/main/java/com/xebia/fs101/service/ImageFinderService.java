package com.xebia.fs101.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageFinderService {
    @Value("${spring.imageFinderservice.clientID}")
    String clientId;
    private RestTemplate restTemplate;

    @Autowired
    public ImageFinderService() {
        this.restTemplate = new RestTemplate();
    }

    @Autowired
    private ObjectMapper objectMapper;

    public String findPicture() {
        String apiUri = "https://api.unsplash.com/photos/random?client_id=";
        ResponseEntity<String> resp = restTemplate.getForEntity(apiUri + clientId, String.class);
        String result = resp.getBody();
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (jsonNode != null) {
            return jsonNode.get("urls").get("regular").toString();
        }
        return "";
    }
}
