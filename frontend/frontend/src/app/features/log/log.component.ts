import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-log',
  templateUrl: './log.component.html',
  styleUrls: ['./log.component.css']
})
export class LogComponent implements OnInit {
  projectPath: string = '';
  errorMessage: string = '';
  commits: any[] = [];

  ngOnInit() {
    const savedPath = localStorage.getItem('projectPath');
    if (savedPath) {
      this.projectPath = savedPath;
    }
  }

  getLog() {
    this.errorMessage = '';
    this.commits = [];

    // Will call API later
    // Mock data
    this.commits = [
      {
        hash: 'abc123def456789abcdef123456789abcdef1234',
        shortHash: 'abc123d',
        author: 'Dolly',
        timestamp: '1711234567',
        message: 'Initial commit with project setup'
      }
    ];
  }

  formatTimestamp(timestamp: string): string {
    const date = new Date(parseInt(timestamp) * 1000);
    return date.toLocaleString('en-IN', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}