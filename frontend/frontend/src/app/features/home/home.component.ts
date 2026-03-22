import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent {
    projectPath: string = '';
  successMessage: string = '';

  setProjectPath() {
    if (this.projectPath.trim()) {
      // Will save to service later
      localStorage.setItem('projectPath', this.projectPath);
      this.successMessage = 'Project path set successfully!';
      
      setTimeout(() => {
        this.successMessage = '';
      }, 3000);
    }
  }
}
