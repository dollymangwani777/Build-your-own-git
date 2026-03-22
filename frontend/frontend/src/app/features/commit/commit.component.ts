import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-commit',
  templateUrl: './commit.component.html',
  styleUrls: ['./commit.component.css']
})
export class CommitComponent implements OnInit {
  projectPath: string = '';
  author: string = '';
  message: string = '';
  successMessage: string = '';
  errorMessage: string = '';
  commitResult: any = null;

  ngOnInit() {
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }

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

    // Will call API later
    // Mock response
    this.commitResult = {
      commitHash: 'abc123def456789abcdef123456789abcdef1234',
      filesChanged: '3',
      message: '[main abc123d] ' + this.message
    };

    this.successMessage = 'Commit created successfully!';
    
    // Clear form
    this.message = '';
  }
}