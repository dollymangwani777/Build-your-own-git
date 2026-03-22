import { Component, OnInit } from '@angular/core';
import { GitService } from 'src/app/services/git.service';

@Component({
  selector: 'app-add',
  templateUrl: './add.component.html',
  styleUrls: ['./add.component.css']
})
export class AddComponent implements OnInit {
  //this variable will hold the project path input by the user which will be extracted from localStorage;
  projectPath: string = '';

  // files selected by the user to be added to staging area, this will be populated when user selects files using file input
  selectedFiles: File[] = [];

  //list of files that have been added to staging area successfully, this will be populated after we call the backend API to add files
  addedFiles: any[] = [];

  //variables to hold success and error messages to display to the user after attempting to add files
  successMessage: string = '';
  errorMessage: string = '';

  //when api call is in progress, we can set this to true to show a loading spinner or disable the add button to prevent multiple submissions
  isLoading: boolean = false;

  constructor(private gitService: GitService) { }

  ngOnInit() {
    //when the page loads, extract the saved project path from localStorage and populate the projectPath variable, so that it can be used when adding files
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }

  onFileSelect(event: any) {
    //when user selects files using the file input, this method is called, it extracts the selected files from the event and populates the selectedFiles array
    //event.target.files is a browser's FileList object, we convert it to an array using Array.from() so that we can easily work with it in our component
    const files = event.target.files;
    this.selectedFiles = Array.from(files);
  }

  //helper method to format file size in a human-readable format (e.g. KB, MB) for display in the UI
  formatFileSize(bytes: number): string {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
  }

  addFiles() {
    //clear previous messages before attempting to add files
    this.successMessage = '';
    this.errorMessage = '';

    //if any file is not selected, show an error message and return early to prevent calling the API with no files which user has not initialized the repository yet
    if (this.selectedFiles.length === 0) {
      this.errorMessage = 'Please select files to add';
      return;
    }

    this.isLoading = true; //set loading state to true while we call the API

    //call the addFiles method in GitService to send the selected files and project path to the backend API
    this.gitService.addFiles(this.selectedFiles, this.projectPath).subscribe({
      next: (response: any) => {
        this.addFiles = response.files; //assuming the backend returns a JSON object with a 'files' property that contains the list of added files with their names and hashes
        this.successMessage = response.message; //assuming the backend also returns a 'message' property with a success message to display to the user

        this.selectedFiles = []; //clear the selected files after successful addition
        this.isLoading = false; //set loading state back to false after API call is complete
      },

      error: (err) => {
        // show backend error msg
        this.errorMessage = err.error?.error;
        this.isLoading = false;
      }
    })

  }
}