package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.services.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class CommitController {
    @Autowired
    CommitService commitService;

    @PostMapping("/commit")
    public ResponseEntity<Map<String, String>> commit(@RequestBody Map<String, String> request){
        try{
            String projectPath = request.get("projectPath");
            String author = request.get("author");
            String message = request.get("message");

            //validate projectpath
            if (projectPath == null || projectPath.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));
            }

            //message validation
            if (message == null || message.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Commit message required"));
            }

            // Validate author
            if (author == null || author.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Author name required"));
            }

            Map<String, String> result = commitService.commit(projectPath, message, author);
            // result contains: commitHash, shortHash, filesChanged
            String responseMessage = "[main " + result.get("shortHash") + "] " + message;

            return ResponseEntity.ok().body(Map.of(
                    "message", responseMessage,
                    "commitHash", result.get("commitHash"),
                    "filesChanged", result.get("filesChanged")
            ));
        }catch(Exception e){
            return ResponseEntity.status(500).body(Map.of("error", "Failed to commit: " + e.getMessage()));
        }
    }
}
