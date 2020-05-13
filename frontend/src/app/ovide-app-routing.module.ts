import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RulesetsOverviewComponent, RulesetCreatorComponent } from './modules/ruleset-management';
import { RulesetIdeComponent } from './modules/ruleset-ide';
import { RulesetTestsuiteComponent } from './modules/ruleset-testsuite';



const routes: Routes = [
  { path: 'rulesets',
    children: [
      { path: '', pathMatch: 'full', component: RulesetsOverviewComponent },
      { path: 'new', component: RulesetCreatorComponent },
      { path: ':id',
        children: [
          { path: 'edit', component: RulesetIdeComponent },
          { path: 'test', component: RulesetTestsuiteComponent }
        ]
      }
    ]
  },
  { path: '', pathMatch: 'full', redirectTo: 'rulesets' },
  { path: '**', redirectTo: 'rulesets' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { enableTracing: false })],
  exports: [RouterModule]
})
export class OvideAppRoutingModule { }
