package com.sgt_hackathon.build_your_own_git.services;

import com.sgt_hackathon.build_your_own_git.exceptions.AlreadyInitializedException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class InitService {
    private final String GIT_DIR = "workspace/.mygit/";

    public void gitInit() throws AlreadyInitializedException {
        //Step 1 : Check if already initialized
        //new File(GIT_DIR) - created file object
        //.exists() - returns true or false if folder exists
        if(new File(GIT_DIR).exists()){
            throw new AlreadyInitializedException();
        }

        //Step 2 : create directory structure
        createDirectory(GIT_DIR); // .mygit/
        createDirectory(GIT_DIR + "objects/");  // .mygit/objects/
        createDirectory(GIT_DIR + "refs/");  // .mygit/refs/
        createDirectory(GIT_DIR + "refs/heads/");  // .mygit/refs/heads/

        try{
            //head file - tracks the current branch
            writeFile(GIT_DIR + "HEAD", "ref: refs/heads/main");

            //index file - staging area
            writeFile(GIT_DIR + "index", "");

            //// config file - repository settings
            writeFile(GIT_DIR + "config", "");
        }catch(IOException e){
            throw new RuntimeException("Failed to create Git files: " + e.getMessage());
        }


    }

    public void createDirectory(String path){
        //creating file object
        File file = new File(path);

        //mkdirs creates parent folders also
        //returns true or false
        boolean created = file.mkdirs();

        //Checking: if not created and does not exist already
        if (!created && !file.exists()) {
            throw new RuntimeException("Failed to create directory: " + path);
        }

        //if folder already exists then mkdirs() will return false
    }

    public void writeFile(String path, String content) throws IOException {
        // Files.write() → Modern Java method (Java 7+)
        // Paths.get(path) → creates path object
        // content.getBytes() → converts string into bytearray
        Files.write(Paths.get(path), content.getBytes());
        //This one line creates and also writes the file and also automatically closes the file
    }
}
