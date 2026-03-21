package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.services.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/git")
@CrossOrigin("*")
public class AddController {
    @Autowired
    AddService addService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam("projectPath") String projectPath){
        try {
            if (projectPath == null || projectPath.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));
            }

            // Multiple files process
            List<Map<String, String>> results = addService.addFiles(files, projectPath);

            Map<String, Object> response = new HashMap<>();
            response.put("message", files.size() + " files added to staging area");
            response.put("files", results);  // ← List of {filename, hash}

            return ResponseEntity.ok().body(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to add files: " + e.getMessage()));
        }
    }
    }
