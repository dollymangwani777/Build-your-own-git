package com.sgt_hackathon.build_your_own_git.services;

import com.sgt_hackathon.build_your_own_git.utils.HashUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AddService {

    public List<Map<String, String>> addFiles(List<MultipartFile> files, String projectPath) throws IOException {
        try {
            // Step 1: Validate project path
            // new File(projectPath) - creates file object for the given path
            File projectFolder = new File(projectPath);

            // .exists() - returns true or false if path exists
            if (!projectFolder.exists()) {
                throw new RuntimeException("Project path does not exist: " + projectPath);
            }

            // Step 2: Check if Git initialized in this project
            // User ne jo path diya, usme .mygit hona chahiye
            String GIT_DIR = projectPath + "/.mygit/";

            if (!new File(GIT_DIR).exists()) {
                throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
            }

            // Step 3: Process all files
            // List to store results for each file
            List<Map<String, String>> results = new ArrayList<>();

            // Loop through each file
            for (MultipartFile file : files) {

                // Extract filename and content
                String filename = file.getOriginalFilename();
                System.out.println("DEBUG - Filename: " + filename);

                // getBytes() → Returns file content as byte array
                // new String(...) → Convert bytes to String
                String content = new String(file.getBytes());

                // Step 4: Creating blob object
                // Git blob format: "blob <size>\0<content>"
                // \0 = null character (separator)
                int size = content.getBytes().length;  // Content size in bytes
                String blobContent = "blob " + size + "\0" + content;

                // Step 5: Calculate sha-1 hash
                // Hash is calculated on ENTIRE blob (not just content)
                String hash = HashUtil.calculateSHA1(blobContent);

                // Step 6: Store blob object in .mygit/objects/
                // Git stores objects in folder structure: objects/ab/cdef123...
                // First 2 characters of hash = folder name

                // Extract first 2 chars for folder
                String folderName = hash.substring(0, 2); // "a1" from "a1b2c3d4..."
                String fileName = hash.substring(2);      // "b2c3d4..." from "a1b2c3d4..."

                // Build paths
                String objectFolder = GIT_DIR + "objects/" + folderName + "/";
                String objectPath = objectFolder + fileName;

                // Create folder if doesn't exist
                // mkdirs() creates parent folders too
                new File(objectFolder).mkdirs();

                // Write blob content to file
                // Paths.get() creates Path object
                // getBytes() converts String to byte array
                Files.write(Paths.get(objectPath), blobContent.getBytes());

                // Step 7: Update index file (staging area)
                // Index format: one line per file
                // Format: "filename hash\n"

                String indexPath = GIT_DIR + "index";
                String indexEntry = filename + " " + hash + "\n";

                // StandardOpenOption.APPEND → Add to end of file (don't overwrite)
                // StandardOpenOption.CREATE → Create file if doesn't exist
                Files.write(
                        Paths.get(indexPath),
                        indexEntry.getBytes(),
                        StandardOpenOption.APPEND,
                        StandardOpenOption.CREATE
                );

                // Step 8: Add result for this file
                // Map with filename and hash for controller response
                results.add(Map.of("filename", filename, "hash", hash));
            }

            // Return all results
            return results;

        } catch (IOException e) {
            // If any file operation fails, throw runtime exception
            throw new RuntimeException("Failed to add file: " + e.getMessage());
        }
    }
}
