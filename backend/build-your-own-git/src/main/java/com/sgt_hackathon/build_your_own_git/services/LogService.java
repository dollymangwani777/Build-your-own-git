package com.sgt_hackathon.build_your_own_git.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogService {

    public Map<String, Object> log(String projectPath) throws IOException {

        String GIT_DIR = projectPath + "/.mygit/";

        //Validate project path and Git initialization
        File projectFolder = new File(projectPath);
        if (!projectFolder.exists()) {
            throw new RuntimeException("Project path does not exist: " + projectPath);
        }

        if (!new File(GIT_DIR).exists()) {
            throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
        }

        //Read current branch pointer (refs/heads/main)
        String branchFile = GIT_DIR + "refs/heads/main";
        File branch = new File(branchFile);

        if (!branch.exists()) {
            // No commits yet
            Map<String, Object> response = new HashMap<>();
            response.put("commits", new ArrayList<>());
            response.put("message", "No commits yet");
            return response;
        }

        // Get latest commit hash from branch pointer
        String currentCommitHash = Files.readString(Paths.get(branchFile)).trim();

        if (currentCommitHash.isEmpty()) {
            // No commits yet
            Map<String, Object> response = new HashMap<>();
            response.put("commits", new ArrayList<>());
            response.put("message", "No commits yet");
            return response;
        }

        //Traverse commit chain (from latest to oldest)
        List<Map<String, String>> commits = new ArrayList<>();
        String commitHash = currentCommitHash;

        while (commitHash != null && !commitHash.isEmpty()) {
            // Read commit object
            String commitFolder = GIT_DIR + "objects/" + commitHash.substring(0, 2) + "/";
            String commitFile = commitFolder + commitHash.substring(2);

            File commitObject = new File(commitFile);
            if (!commitObject.exists()) {
                break;  // Commit object not found - stop
            }

            // Read commit content
            String commitContent = Files.readString(Paths.get(commitFile));

            // Parse commit content
            // Format:
            // tree <hash>
            // parent <hash> (optional)
            // author <name> <timestamp>
            //
            // <message>

            String[] lines = commitContent.split("\n");

            String treeHash = null;
            String parentHash = null;
            String author = null;
            String timestamp = null;
            StringBuilder messageBuilder = new StringBuilder();
            boolean messageSection = false;

            for (String line : lines) {
                if (line.startsWith("tree ")) {
                    treeHash = line.substring(5).trim();
                } else if (line.startsWith("parent ")) {
                    parentHash = line.substring(7).trim();
                } else if (line.startsWith("author ")) {
                    // Format: "author Dolly 1710987654"
                    String authorLine = line.substring(7).trim();
                    String[] authorParts = authorLine.split(" ");
                    if (authorParts.length >= 2) {
                        author = authorParts[0];
                        timestamp = authorParts[1];
                    } else {
                        author = authorLine;
                    }
                } else if (line.trim().isEmpty()) {
                    messageSection = true;  // Empty line - message starts after this
                } else if (messageSection) {
                    messageBuilder.append(line);
                }
            }

            // Add commit to list
            Map<String, String> commitInfo = new HashMap<>();
            commitInfo.put("hash", commitHash);
            commitInfo.put("shortHash", commitHash.substring(0, 7));
            commitInfo.put("author", author != null ? author : "Unknown");
            commitInfo.put("timestamp", timestamp != null ? timestamp : "0");
            commitInfo.put("message", messageBuilder.toString().trim());

            commits.add(commitInfo);

            // Move to parent commit (if exists)
            commitHash = parentHash;
        }

        //Return result
        Map<String, Object> response = new HashMap<>();
        response.put("commits", commits);

        return response;
    }
}