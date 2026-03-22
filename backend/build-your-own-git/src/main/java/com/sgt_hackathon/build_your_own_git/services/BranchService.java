package com.sgt_hackathon.build_your_own_git.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class BranchService {

   //list all branches
    public Map<String, Object> listBranches(String projectPath) throws IOException {

        String GIT_DIR = projectPath + "/.mygit/";

        // Validate
        File projectFolder = new File(projectPath);
        if (!projectFolder.exists()) {
            throw new RuntimeException("Project path does not exist: " + projectPath);
        }

        if (!new File(GIT_DIR).exists()) {
            throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
        }

        // Read all branches from refs/heads/ folder
        String branchesDir = GIT_DIR + "refs/heads/";
        File branchesDirFile = new File(branchesDir);

        List<String> branches = new ArrayList<>();

        if (branchesDirFile.exists() && branchesDirFile.isDirectory()) {
            File[] branchFiles = branchesDirFile.listFiles();
            if (branchFiles != null) {
                for (File branchFile : branchFiles) {
                    if (branchFile.isFile()) {
                        branches.add(branchFile.getName());
                    }
                }
            }
        }

        // Get current branch from HEAD file
        String headFile = GIT_DIR + "HEAD";
        String currentBranch = "main";  // Default

        if (new File(headFile).exists()) {
            String headContent = Files.readString(Paths.get(headFile)).trim();
            // Format: "ref: refs/heads/main"
            if (headContent.startsWith("ref: refs/heads/")) {
                currentBranch = headContent.substring("ref: refs/heads/".length());
            }
        }

        // Return result
        Map<String, Object> response = new HashMap<>();
        response.put("branches", branches);
        response.put("currentBranch", currentBranch);

        return response;
    }

    //function for creating a new branch
    public Map<String, Object> createBranch(String projectPath, String branchName) throws IOException {

        String GIT_DIR = projectPath + "/.mygit/";

        // Validate
        File projectFolder = new File(projectPath);
        if (!projectFolder.exists()) {
            throw new RuntimeException("Project path does not exist: " + projectPath);
        }

        if (!new File(GIT_DIR).exists()) {
            throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
        }

        // Check if branch already exists
        String branchFile = GIT_DIR + "refs/heads/" + branchName;
        if (new File(branchFile).exists()) {
            throw new RuntimeException("Branch '" + branchName + "' already exists");
        }

        // Get current commit hash (from current branch pointer)
        // Read HEAD to find current branch
        String headFile = GIT_DIR + "HEAD";
        String currentBranch = "main";

        if (new File(headFile).exists()) {
            String headContent = Files.readString(Paths.get(headFile)).trim();
            if (headContent.startsWith("ref: refs/heads/")) {
                currentBranch = headContent.substring("ref: refs/heads/".length());
            }
        }

        // Read current branch's commit hash
        String currentBranchFile = GIT_DIR + "refs/heads/" + currentBranch;

        if (!new File(currentBranchFile).exists()) {
            throw new RuntimeException("No commits yet. Cannot create branch.");
        }

        String currentCommitHash = Files.readString(Paths.get(currentBranchFile)).trim();

        // Create new branch file with same commit hash
        Files.write(Paths.get(branchFile), currentCommitHash.getBytes());

        // Return result
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Branch '" + branchName + "' created");
        response.put("branchName", branchName);

        return response;
    }
}