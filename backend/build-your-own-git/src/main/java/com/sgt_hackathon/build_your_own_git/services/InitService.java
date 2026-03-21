package com.sgt_hackathon.build_your_own_git.services;

import com.sgt_hackathon.build_your_own_git.exceptions.AlreadyInitializedException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class InitService {

    public void gitInit(String projectPath) throws AlreadyInitializedException {

        // Step 1: Validate project path
        // new File(projectPath) - creates file object for the given path
        File projectFolder = new File(projectPath);

        // .exists() - returns true or false if path exists
        if (!projectFolder.exists()) {
            throw new RuntimeException("Path does not exist: " + projectPath);
        }

        // .isDirectory() - checks if path is a folder (not a file)
        if (!projectFolder.isDirectory()) {
            throw new RuntimeException("Path is not a directory: " + projectPath);
        }

        // Step 2: Create .mygit path in user's project folder
        // User ne jo path diya, usme .mygit banayenge
        String GIT_DIR = projectPath + "/.mygit/";

        // Step 3: Check if already initialized
        // new File(GIT_DIR) - created file object
        // .exists() - returns true or false if folder exists
        if (new File(GIT_DIR).exists()) {
            throw new AlreadyInitializedException();
        }

        // Step 4: Create directory structure
        createDirectory(GIT_DIR);                    // .mygit/
        createDirectory(GIT_DIR + "objects/");       // .mygit/objects/
        createDirectory(GIT_DIR + "refs/");          // .mygit/refs/
        createDirectory(GIT_DIR + "refs/heads/");    // .mygit/refs/heads/

        // Step 5: Create files
        try {
            // head file - tracks the current branch
            writeFile(GIT_DIR + "HEAD", "ref: refs/heads/main");

            // index file - staging area
            writeFile(GIT_DIR + "index", "");

            // config file - repository settings
            writeFile(GIT_DIR + "config", "");
        } catch (IOException e) {
            throw new RuntimeException("Failed to create Git files: " + e.getMessage());
        }
    }

    private void createDirectory(String path) {
        // creating file object
        File file = new File(path);

        // mkdirs creates parent folders also
        // returns true or false
        boolean created = file.mkdirs();

        // Checking: if not created and does not exist already
        if (!created && !file.exists()) {
            throw new RuntimeException("Failed to create directory: " + path);
        }

        // if folder already exists then mkdirs() will return false
    }

    private void writeFile(String path, String content) throws IOException {
        // Files.write() → Modern Java method (Java 7+)
        // Paths.get(path) → creates path object
        // content.getBytes() → converts string into bytearray
        Files.write(Paths.get(path), content.getBytes());
        // This one line creates and also writes the file and also automatically closes the file
    }
}