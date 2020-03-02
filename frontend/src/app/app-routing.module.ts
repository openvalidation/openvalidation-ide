import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RulesetsOverviewComponent } from './rulesets-overview/rulesets-overview.component';
import { RulesetEditorComponent } from './ruleset-editor/ruleset-editor.component';
import { RulesetCreatorComponent } from './ruleset-creator/ruleset-creator.component';


const routes: Routes = [
  { path: 'rulesets',
    children:[
      { path: '', pathMatch: 'full', component: RulesetsOverviewComponent },
      { path: 'new', component: RulesetCreatorComponent },
      { path: ':id', component: RulesetEditorComponent },
    ]
  },
  { path: '', pathMatch: 'full', redirectTo: 'rulesets' } ,
  { path: '**', redirectTo: 'rulesets' } 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
