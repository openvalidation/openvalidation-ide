import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RulesetsOverviewComponent } from './rulesets-overview/rulesets-overview.component';
import { RulesetEditorComponent } from './ruleset-editor/ruleset-editor.component';
import { RulesetCreatorComponent } from './ruleset-creator/ruleset-creator.component';
import { OvideAppComponent } from './ovide-app/ovide-app.component';

@NgModule({
  declarations: [
    OvideAppComponent,
    RulesetsOverviewComponent,
    RulesetEditorComponent,
    RulesetCreatorComponent,
    OvideAppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [OvideAppComponent]
})
export class OvideModule { }
