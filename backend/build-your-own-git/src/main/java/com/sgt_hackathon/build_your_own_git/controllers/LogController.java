package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class LogController {

    @Autowired
    LogService logService;

    @PostMapping("/log")
    public ResponseEntity<Map<String, Object>> log(@RequestBody Map<String, String> request) {
        try {
            // Extract project path
            String projectPath = request.get("projectPath");

            // Validate
            if (projectPath == null || projectPath.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));
            }

            // Call service
            Map<String, Object> result = logService.log(projectPath);

            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get log: " + e.getMessage()));
        }
    }
}