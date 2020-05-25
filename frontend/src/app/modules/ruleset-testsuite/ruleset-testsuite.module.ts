import { NgModule } from '@angular/core';
import { SharedModule } from '@ovide/shared';
import { RulesetTestsuiteComponent } from './components/ruleset-testsuite/ruleset-testsuite.component';
import { GaugeChartComponent } from './components/gauge-chart/gauge-chart.component';
import { RulesetEditorModule } from '../ruleset-editor';



@NgModule({
  declarations: [
    RulesetTestsuiteComponent,
    GaugeChartComponent
  ],
  imports: [
    SharedModule,
    RulesetEditorModule
  ],
  exports: [
    RulesetTestsuiteComponent
  ]
})
export class RulesetTestsuiteModule { }
