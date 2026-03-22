import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-branch',
  templateUrl: './branch.component.html',
  styleUrls: ['./branch.component.css']
})
export class BranchComponent implements OnInit {
  projectPath: string = '';
  branches: string[] = [];
  currentBranch: string = '';
  newBranchName: string = '';
  successMessage: string = '';
  errorMessage: string = '';

  ngOnInit() {
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }

  listBranches() {
    this.errorMessage = '';
    
    // Will call API later
    // Mock data
    this.branches = ['main'];
    this.currentBranch = 'main';
  }

  createBranch() {
    this.successMessage = '';
    this.errorMessage = '';

    if (!this.newBranchName.trim()) {
      this.errorMessage = 'Please enter a branch name';
      return;
    }

    // Will call API later
    this.branches.push(this.newBranchName);
    this.successMessage = 'Branch "' + this.newBranchName + '" created successfully!';
    this.newBranchName = '';
  }

  checkoutBranch(branchName: string) {
    if (branchName === this.currentBranch) {
      return;
    }

    // Will call API later
    this.currentBranch = branchName;
    this.successMessage = 'Switched to branch "' + branchName + '"';
  }
}