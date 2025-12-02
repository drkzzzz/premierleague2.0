package com.premier.league.premier_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:5173", "*" })
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", "UP");
        response.put("message", "Backend Premier League está en línea");
        return ResponseEntity.ok(response);
    }
}
