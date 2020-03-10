import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { MaterialDesignModule } from '@ovide/material-design';
import { OvideAppComponent } from '@ovide/ovide-app';
import { OvideRoutingModule } from '@ovide/routing';

import { RulesetCreatorComponent } from '@ovide/ruleset-creator';
import { RulesetEditorComponent } from '@ovide/ruleset-editor';
import { RulesetsOverviewComponent } from '@ovide/rulesets-overview';
import { SchemaEditorComponent } from '@ovide/schema-editor';
import { SchemaAttributeDialogComponent } from './schema-attribute-dialog/schema-attribute-dialog.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RulesetTestsuiteComponent } from './ruleset-testsuite/ruleset-testsuite.component';

@NgModule({
  declarations: [
    OvideAppComponent,
    RulesetsOverviewComponent,
    RulesetEditorComponent,
    RulesetCreatorComponent,
    SchemaEditorComponent,
    SchemaAttributeDialogComponent,
    RulesetTestsuiteComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    OvideRoutingModule,
    MaterialDesignModule,
    NgxChartsModule
  ],
  providers: [],
  bootstrap: [OvideAppComponent]
})
export class OvideModule { }
