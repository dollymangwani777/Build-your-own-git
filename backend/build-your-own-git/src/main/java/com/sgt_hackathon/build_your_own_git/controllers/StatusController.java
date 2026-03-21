package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class StatusController {
    @Autowired
    StatusService statusService;

    @PostMapping("/status")
    public ResponseEntity<Map<String, Object>> status(@RequestBody Map<String, String> request){
        try{
            //extract  project path
            String projectPath = request.get("projectPath");

            //validate
            if(projectPath == null || projectPath.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));

            //calling service
            Map<String, Object> result = statusService.status(projectPath);
            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to get status: " + e.getMessage()));
        }
    }


}
