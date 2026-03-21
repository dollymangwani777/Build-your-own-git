package com.sgt_hackathon.build_your_own_git.exceptions;

public class AlreadyInitializedException extends Exception {
  public AlreadyInitializedException() {
    super("Git repository already exists in this directory");
  }
}
