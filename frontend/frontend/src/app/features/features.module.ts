import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { InitComponent } from './init/init.component';
import { AddComponent } from './add/add.component';
import { CommitComponent } from './commit/commit.component';
import { StatusComponent } from './status/status.component';
import { LogComponent } from './log/log.component';
import { BranchComponent } from './branch/branch.component';
import { FormsModule } from '@angular/forms';



@NgModule({
  declarations: [
    HomeComponent,
    InitComponent,
    AddComponent,
    CommitComponent,
    StatusComponent,
    LogComponent,
    BranchComponent
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    HomeComponent,
    InitComponent,
    AddComponent,
    CommitComponent,
    StatusComponent,
    LogComponent,
    BranchComponent
  ]
})
export class FeaturesModule { }
