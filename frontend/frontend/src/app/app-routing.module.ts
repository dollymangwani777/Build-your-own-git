import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddComponent } from './features/add/add.component';
import { BranchComponent } from './features/branch/branch.component';
import { CommitComponent } from './features/commit/commit.component';
import { HomeComponent } from './features/home/home.component';
import { InitComponent } from './features/init/init.component';
import { LogComponent } from './features/log/log.component';
import { StatusComponent } from './features/status/status.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'init', component: InitComponent },
  { path: 'add', component: AddComponent },
  { path: 'commit', component: CommitComponent },
  { path: 'status', component: StatusComponent },
  { path: 'log', component: LogComponent },
  { path: 'branch', component: BranchComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
