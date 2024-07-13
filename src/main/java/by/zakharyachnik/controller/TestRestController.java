package by.zakharyachnik.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestRestController {

    private final ObjectMapper objectMapper;

    public TestRestController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostMapping("/api/test")
    public ResponseEntity<String> handlePostRequest(@RequestBody String requestPayload) {
        // Обработка полученного запроса и формирование ответа
        String response = "Received POST request with payload: " + requestPayload;

        // Создание JSON-объекта для ответа
        ResponsePayload jsonResponse = new ResponsePayload(response);

        // Преобразование объекта в JSON-строку
        String json;
        try {
            json = objectMapper.writeValueAsString(jsonResponse);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing response");
        }

        // Возврат ответа в формате JSON
        return ResponseEntity.ok(json);
    }

    public class ResponsePayload {
        private String response;

        public ResponsePayload(String response) {
            this.response = response;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }
}
