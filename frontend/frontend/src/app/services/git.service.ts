import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GitService {

  // Base URL of our Spring Boot backend, All API calls will start with this
  private baseUrl = 'http://localhost:8080/api/git'

  // HttpClient is Angular's built-in tool to make HTTP requests (GET, POST etc)
  constructor(private http: HttpClient) { }

   // http.post() sends a POST request to the backend
   // First argument = URL, Second argument = body (data we're sending)
    // We're sending projectPath as JSON like: { "projectPath": "C:/Users/..." }
  initRepo(path: string){
    return this.http.post(`${this.baseUrl}/init`, { projectPath: path })
  }


  addFiles(files: File[], projectPath:string){
    //to send files to backend, we need to use FormData, which allows us to send files as multipart/form-data
    //we cannot send files as JSON, so we append them to FormData
    const formData = new FormData()

    //add each file to formData
    //'files' should match the parameter name in the backend controller @RequestParam("files")
    files.forEach(file => formData.append('files', file))

    //also append projectPath to formData
    //projectPath should match the parameter name in the backend controller @RequestParam("projectPath")
    formData.append('projectPath', projectPath)

    //send post request to backend with files
    return this.http.post(`${this.baseUrl}/add`, formData)
  }
}
