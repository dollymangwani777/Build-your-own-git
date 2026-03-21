package com.sgt_hackathon.build_your_own_git.services;

import com.sgt_hackathon.build_your_own_git.utils.HashUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Map;

@Service
public class CommitService {
    public Map<String, String> commit(String projectPath, String message, String author) throws IOException {
       try{
           //getting directory path
           String GIT_DIR = projectPath + "/.mygit/";

           //validate project and git initialisation
           File projectFolder = new File(projectPath);
           if(!projectFolder.exists()){
               throw new RuntimeException("Project path does not exist: " + projectPath);
           }

           if (!new File(GIT_DIR).exists()) {
               throw new RuntimeException("Git not initialized in this project. Run 'init' first.");
           }

           //reading index file(staging area)
           String indexPath = GIT_DIR + "index";
           File indexFile = new File(indexPath);

           if (!indexFile.exists() || indexFile.length() == 0) {
               throw new RuntimeException("No files staged for commit. Use 'add' first.");
           }

           String indexContent = Files.readString(Paths.get(indexPath));//Paths.get(indexPath) used to convert a path string into a Path object and Files.readString() is used to read the entire content of a file into a single String
           String[] indexLines = indexContent.trim().split("\n");//clean a string of leading/trailing whitespace and then break it into an array of lines based on the newline character.

           // Count files changed
           int filesChanged = indexLines.length;

           //Create tree content from index
           // Tree format: "tree <size>\0<entries>"
           // Entry format: "100644 blob <hash> <filename>\n"
           StringBuilder treeEntries = new StringBuilder();

           for (String line : indexLines) {
               // Line format: "filename hash"
               String[] parts = line.trim().split(" ");
               if (parts.length != 2) {
                   continue;  // Skip invalid lines
               }

               String filename = parts[0];
               String blobHash = parts[1];

               // Tree entry format: "100644 blob hash filename\n"
               // 100644 = file permissions (normal file)
               treeEntries.append("100644 blob ").append(blobHash).append(" ").append(filename).append("\n");
           }

           // Complete tree content with header
           String treeContent = "tree " + treeEntries.length() + "\0" + treeEntries.toString();

           //Calculate tree hash
           String treeHash = HashUtil.calculateSHA1(treeContent);

           //store tree object in objects folder in .mygit
           String treeFolderName = treeHash.substring(0,2);
           String treeFileName = treeHash.substring(2);
           String treeObjectFolder = GIT_DIR + "objects/" + treeFolderName + "/";
           String treeObjectPath = treeObjectFolder + treeFileName;

           // Create folder if doesn't exist
           new File(treeObjectFolder).mkdirs();

           // Write tree object to file
           Files.write(Paths.get(treeObjectPath), treeContent.getBytes());

           //Get parent commit (if exists)
           // Read refs/heads/main to get current commit hash
           String branchFile = GIT_DIR + "refs/heads/main";
           String parentCommit = null;

           File branch = new File(branchFile);
           if (branch.exists()) {
               String branchContent = Files.readString(Paths.get(branchFile)).trim();
               if (!branchContent.isEmpty()) {
                   parentCommit = branchContent;  // Parent commit found
               }
           }

           // Create commit content
           // Commit format: tree <tree-hash>
           // parent <parent-hash> (if exists)
           // author <name> <timestamp>
           // <commit-message>
           // Get current timestamp (Unix epoch seconds)
           long timestamp = Instant.now().getEpochSecond();

           StringBuilder commitContent = new StringBuilder();
           commitContent.append("tree ").append(treeHash).append("\n");

           // Add parent if exists (not first commit)
           if (parentCommit != null) {
               commitContent.append("parent ").append(parentCommit).append("\n");
           }

           commitContent.append("author ").append(author).append(" ").append(timestamp).append("\n");
           commitContent.append("\n");
           commitContent.append(message);


           //calculate commit hash
           String commitHash = HashUtil.calculateSHA1(commitContent.toString());


           //Store commit object
           // Path: .mygit/objects/12/3456...
           String commitFolderName = commitHash.substring(0, 2);
           String commitFileName = commitHash.substring(2);
           String commitObjectFolder = GIT_DIR + "objects/" + commitFolderName + "/";
           String commitObjectPath = commitObjectFolder + commitFileName;


           // Create folder if doesn't exist
           new File(commitObjectFolder).mkdirs();

           // Write commit object to file
           Files.write(Paths.get(commitObjectPath), commitContent.toString().getBytes()); //since stringbuilder do not have getBytes() method only string has so we are converting stringbuilder to string first then converting to byteArray


           //Update branch pointer (refs/heads/main)
           // Overwrite with new commit hash
           Files.write(Paths.get(branchFile), commitHash.getBytes());

           //Clear index file (staging area)
           // After commit, staging area should be empty
           Files.write(Paths.get(indexPath), "".getBytes());

           // Return result
           // shortHash = first 7 characters of commit hash (for display)
           String shortHash = commitHash.substring(0, 7);

           return Map.of(
                   "commitHash", commitHash,
                   "shortHash", shortHash,
                   "filesChanged", String.valueOf(filesChanged)
           );
       }catch (IOException e) {
           throw new RuntimeException("Failed to create commit: " + e.getMessage());
       }
    }

}
