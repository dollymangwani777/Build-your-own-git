package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.services.CheckoutService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class CheckoutController {
    @Autowired
    CheckoutService checkoutService;
    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(@RequestBody Map<String, String> request){
        try{
            // Extract parameters
            String projectPath = request.get("projectPath");
            String branchName = request.get("branchName");

            //validate
            if (projectPath == null || projectPath.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Project path required"));
            }

            if (branchName == null || branchName.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Branch name required"));
            }

            //calling service
            Map<String, String> result = checkoutService.checkout(projectPath, branchName);

            return ResponseEntity.ok().body(result);
        }catch(Exception e)
        {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to checkout: " + e.getMessage()));
        }
    }

}
