package com.sgt_hackathon.build_your_own_git.controllers;

import com.sgt_hackathon.build_your_own_git.exceptions.AlreadyInitializedException;
import com.sgt_hackathon.build_your_own_git.services.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/git")
public class InitController {
    @Autowired
    InitService initService;

    @PostMapping("/init")
    public ResponseEntity<Map<String, String>> gitInit(){
        try{
            initService.gitInit();
            return ResponseEntity.ok().body(Map.of("message", "Successfully initialized git!"));
        }catch(AlreadyInitializedException e){
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to initialize: " + e.getMessage()));
        }
    }
}
