import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { MaterialDesignModule } from '@ovide/material-design';
import { OvideAppComponent } from '@ovide/ovide-app';
import { OvideRoutingModule } from '@ovide/routing';

import { RulesetCreatorComponent } from '@ovide/ruleset-creator';
import { RulesetEditorComponent } from '@ovide/ruleset-editor';
import { RulesetsOverviewComponent } from '@ovide/rulesets-overview';
import { SchemaEditorComponent } from '@ovide/schema-editor';

@NgModule({
  declarations: [
    OvideAppComponent,
    RulesetsOverviewComponent,
    RulesetEditorComponent,
    RulesetCreatorComponent,
    SchemaEditorComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    OvideRoutingModule,
    MaterialDesignModule
  ],
  providers: [],
  bootstrap: [OvideAppComponent]
})
export class OvideModule { }
