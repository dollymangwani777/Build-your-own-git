package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.exceptions.AlreadyInitializedException;
import com.sgt_hackathon.build_your_own_git.services.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class InitController {
    @Autowired
    InitService initService;

    @PostMapping("/init")
    public ResponseEntity<Map<String, String>> gitInit(@RequestBody Map<String, String> request) {
        try {
            // Extract project path from request
            String projectPath = request.get("projectPath");

            // Validate
            if (projectPath == null || projectPath.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));
            }  

            // Call service with project path
            initService.gitInit(projectPath);  // ← passed project path

            return ResponseEntity.ok().body(Map.of(
                    "message", "Successfully initialized git at: " + projectPath
            ));

        } catch (AlreadyInitializedException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to initialize: " + e.getMessage()));
        }
    }
}