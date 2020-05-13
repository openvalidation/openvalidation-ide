import { NgModule } from '@angular/core';
import { SharedModule } from '@ovide/shared';
import { RulesetCreatorComponent } from './components/ruleset-creator/ruleset-creator.component';
import { RulesetsOverviewComponent } from './components/rulesets-overview/rulesets-overview.component';



@NgModule({
  declarations: [
    RulesetCreatorComponent,
    RulesetsOverviewComponent
  ],
  imports: [
    SharedModule
  ],
  exports: [
    RulesetCreatorComponent,
    RulesetsOverviewComponent
  ]
})
export class RulesetManagementModule { }
