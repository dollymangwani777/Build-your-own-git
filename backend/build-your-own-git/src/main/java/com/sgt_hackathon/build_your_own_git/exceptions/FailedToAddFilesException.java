package com.sgt_hackathon.build_your_own_git.exceptions;

public class FailedToAddFilesException extends Exception {
    public FailedToAddFilesException() {
        super("Failed to add files in staging area!");
    }
}
