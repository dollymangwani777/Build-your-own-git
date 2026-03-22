import { Component, OnInit } from '@angular/core';
import { GitService } from 'src/app/services/git.service';

@Component({
  selector: 'app-init',
  templateUrl: './init.component.html',
  styleUrls: ['./init.component.css']
})
export class InitComponent implements OnInit {
  // this variable will hold the user input for project path
  projectPath: string = '';

  // to show success and error messages
  successMessage: string = '';
  errorMessage: string = '';

  // disable button when request is in progress
  isLoading: boolean = false;

  // injecting gitservice to call backend api
  constructor(private gitService: GitService) { }

  ngOnInit() {
    // when page loads, check if there is a saved path in localStorage
    // if yes then set it to projectPath variable so that it is shown in the input field
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }

  initRepository() {
    // clear previous messages
    this.successMessage = '';
    this.errorMessage = '';

    // if input field is empty, show error message and return
    if (!this.projectPath.trim()) {
      this.errorMessage = 'Please enter a project path';
      return;
    }

    this.isLoading = true;

    // in windows backslashes are used in paths, but Java backend needs forward slashes, so replacing backslashes with forward slashes
    const cleanPath = this.projectPath.replace(/\\/g, '/');

    // remove extra spaces from start and end of the path
    // Jaise "C:/Users/Dolly/Desktop/foo project  " → "C:/Users/Dolly/Desktop/foo project"
    const finalPath = cleanPath.trim();

    // send post request to backend to initialize git repository
    this.gitService.initRepo(finalPath).subscribe({
      next: (response: any) => {
        // save the path in localStorage for future use
        localStorage.setItem('projectPath', finalPath);
        this.successMessage = response.message;
        this.isLoading = false;
      },
      error: (err) => {
        // show backend error message
        this.errorMessage = err.error?.error;
        this.isLoading = false;
      }
    });
  }
}