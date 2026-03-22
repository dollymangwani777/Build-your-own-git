package com.sgt_hackathon.build_your_own_git.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class CheckoutService {
    public Map<String, String> checkout(String projectPath, String branchName) throws IOException {
        String GIT_DIR = projectPath + "/.mygit/";

        // STEP 1: Validate project path and git initialisation
        File projectFolder = new File(projectPath);
        if (!projectFolder.exists()) {
            throw new RuntimeException("Project path does not exist: " + projectPath);
        }

        if (!new File(GIT_DIR).exists()) {
            throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
        }

        // STEP 2: Check if branch exists ← YEH ADD KARO!
        String branchFile = GIT_DIR + "refs/heads/" + branchName;

        if (!new File(branchFile).exists()) {
            throw new RuntimeException("Branch '" + branchName + "' does not exist");
        }

        // STEP 3: Check if already on this branch
        String headFile = GIT_DIR + "HEAD";
        String currentBranch = null;

        if (new File(headFile).exists()) {
            String headContent = Files.readString(Paths.get(headFile)).trim();
            // BUG FIX: "ref:" NOT "ref :" (no space!)
            if (headContent.startsWith("ref: refs/heads/")) {  // ← Space NAHI hai!
                currentBranch = headContent.substring("ref: refs/heads/".length());
            }
        }

        if (branchName.equals(currentBranch)) {
            throw new RuntimeException("Already on branch '" + branchName + "'");
        }

        // STEP 4: Update HEAD file to point to new branch
        String newHeadContent = "ref: refs/heads/" + branchName;
        Files.write(Paths.get(headFile), newHeadContent.getBytes());

        // STEP 5: Return result
        Map<String, String> response = new HashMap<>();
        response.put("message", "Switched to branch '" + branchName + "'");
        response.put("branchName", branchName);

        return response;
    }
}