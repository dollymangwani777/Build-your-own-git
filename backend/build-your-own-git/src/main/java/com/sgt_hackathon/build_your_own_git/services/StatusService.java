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
public class StatusService {
    public Map<String, Object> status(String projectPath) throws IOException {
        String GIT_DIR = projectPath + "/.mygit/";
        File projectFolder = new File(projectPath);
        if (!projectFolder.exists()) {
            throw new RuntimeException("Project path does not exist: " + projectPath);
        }

        if (!new File(GIT_DIR).exists()) {
            throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
        }

        // Reading index file
        String indexPath = GIT_DIR + "index";
        File indexFile = new File(indexPath);

        // Checking if index is empty or does not exist
        if (!indexFile.exists() || indexFile.length() == 0) {
            Map<String, Object> response = new HashMap<>();  // ← Object, not String
            response.put("message", "nothing to commit, working tree clean");
            response.put("stagedFiles", new ArrayList<>());  // Empty list
            return response;
        }

        // If Index has files - read and parse
        String indexContent = Files.readString(Paths.get(indexPath));
        String[] indexLines = indexContent.trim().split("\n");

        // Extract filenames from index
        List<String> stagedFiles = new ArrayList<>();
        for (String line : indexLines) {
            // Line format: "filename hash"
            String[] parts = line.trim().split(" ");
            if (parts.length >= 1) {
                stagedFiles.add(parts[0]);  // Filename
            }
        }  // ← Loop ends here

        // Return result (LOOP KE BAHAR!)
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Changes to be committed:");
        response.put("stagedFiles", stagedFiles);

        return response;
    }
}