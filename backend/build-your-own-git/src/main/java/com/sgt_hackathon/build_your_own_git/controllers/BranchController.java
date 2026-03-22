package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class BranchController {
    @Autowired
    BranchService branchService;

    @PostMapping("/branch")
    public ResponseEntity<Map<String, Object>> branch(@RequestBody Map<String, String> request){
        try{
            //extract project path
            String projectPath = request.get("projectPath");
            String action = request.get("action"); // list or create

            //validate
            if (projectPath == null || projectPath.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));
            }

            if (action == null || action.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Action required (list or create)"));
            }

            // Call service based on action
            Map<String, Object> result;

            if("list".equals(action)){
                result = branchService.listBranches(projectPath);
            }else if("create".equals(action)){
                String branchName = request.get("branchName");

                if (branchName == null || branchName.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("error", "Branch name required for create action"));
                }

                result = branchService.createBranch(projectPath, branchName);
            }else {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid action. Use 'list' or 'create'"));
            }

            return ResponseEntity.ok().body(result);
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to execute branch command: " + e.getMessage()));
        }

    }

}
