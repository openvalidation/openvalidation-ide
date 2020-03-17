import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RulesetsOverviewComponent } from '@ovide/rulesets-overview';
import { RulesetEditorComponent } from '@ovide/ruleset-editor';
import { RulesetCreatorComponent } from '@ovide/ruleset-creator';
import { RulesetTestsuiteComponent } from '@ovide/ruleset-testsuite';
import { DesignTestComponent } from '@ovide/design-test/design-test.component';


const routes: Routes = [
  { path: 'design-test', component: DesignTestComponent },
  { path: 'rulesets',
    children: [
      { path: '', pathMatch: 'full', component: RulesetsOverviewComponent },
      { path: 'new', component: RulesetCreatorComponent },
      { path: ':id',
        children: [
          { path: 'edit', component: RulesetEditorComponent },
          { path: 'test', component: RulesetTestsuiteComponent }
        ]
      }
    ]
  },
  { path: '', pathMatch: 'full', redirectTo: 'rulesets' },
  { path: '**', redirectTo: 'rulesets' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class OvideRoutingModule { }
