import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {
  projectPath: string = '';
  errorMessage: string = '';
  statusResult: any = null;

  ngOnInit() {
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }

  getStatus() {
    this.errorMessage = '';
    this.statusResult = null;

    // Will call API later
    // Mock response
    this.statusResult = {
      message: 'nothing to commit, working tree clean',
      stagedFiles: []
    };
  }
}