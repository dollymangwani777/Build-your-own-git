import { Component, OnInit } from '@angular/core';
import { GitService } from 'src/app/services/git.service';

@Component({
  selector: 'app-commit',
  templateUrl: './commit.component.html',
  styleUrls: ['./commit.component.css']
})
export class CommitComponent implements OnInit {
  //this variable will hold the project path input by the user which will be extracted from localStorage;
  projectPath: string = '';

  //variables to hold the author name and commit message input by the user, these will be used when calling the backend API to create a commit
  author: string = '';
  message: string = '';

  //variables to hold success and error messages to display to the user after attempting to create a commit
  successMessage: string = '';
  errorMessage: string = '';

  //this variable will hold the result returned from backend after committing, it can include details like commit hash, files changed etc which we can display to the user
  commitResult: any = null;

  isLoading: boolean = false; //when api call is in progress, we can set this to true to show a loading spinner or disable the commit button to prevent multiple submissions

  //injecting GitService to call the backend API for committing changes, we will implement the commit() method to call gitService.commitRepo() and handle the response to update the UI accordingly
  constructor(private gitService: GitService) { }


  //when the page loads, extract the saved project path from localStorage and populate the projectPath variable, so that it can be used when creating a commit
  ngOnInit() {
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }


  //clear previous messages before attempting to create a commit, validate that the user has entered a commit message and author name, if validation passes, we will call the backend API to create a commit and handle the response to update the UI with success or error messages and display the commit result returned from the backend
  commit() {
    this.successMessage = '';
    this.errorMessage = '';
    this.commitResult = null;

    if (!this.message.trim()) {
      this.errorMessage = 'Please enter a commit message';
      return;
    }

    if (!this.author.trim()) {
      this.errorMessage = 'Please enter author name';
      return;
    }

    
    this.isLoading = true;

    //sending post request to backend to create a commit with the project path, author name and commit message, we will subscribe to the response to handle success and error cases
    this.gitService.commitRepo(this.projectPath, this.author, this.message).subscribe({
      next: (response:any) =>{
        //save the commit details returned from backend in commitResult variable to display to the user
        this.commitResult = {
          commitHash : response.commitHash,
          filesChanged : response.filesChanged,
          message : response.message
        };
      
        this.successMessage = 'Commit created successfully!';

        // clear the response after commiting, 
        // keep the author name in the input field for convenience if user wants to make multiple commits, but clear the commit message field
        this.message = '';
        this.isLoading = false;
      },
      error: (error) => {
        //show error msg of backend
        this.errorMessage = error.error?.error;
        this.isLoading = false;
      }
    })
  }
}