import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialDesignModule } from '@ovide/material-design';
import { OvideAppComponent } from '@ovide/ovide-app';
import { OvideRoutingModule } from '@ovide/routing';
import { RulesetCreatorComponent } from '@ovide/ruleset-creator';
import { RulesetEditorComponent } from '@ovide/ruleset-editor';
import { RulesetsOverviewComponent } from '@ovide/rulesets-overview';
import { SchemaEditorComponent } from '@ovide/schema-editor';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { OvideBackendApiModule, BASE_PATH } from '@ovide/backend';
import { RulesetTestsuiteComponent } from '@ovide/ruleset-testsuite';
import { SchemaAttributeDialogComponent } from '@ovide/schema-attribute-dialog';
import { environment } from 'environments/environment';
import { DesignTestComponent } from './design-test/design-test.component';

@NgModule({
  declarations: [
    OvideAppComponent,
    RulesetsOverviewComponent,
    RulesetEditorComponent,
    RulesetCreatorComponent,
    SchemaEditorComponent,
    SchemaAttributeDialogComponent,
    RulesetTestsuiteComponent,
    DesignTestComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    OvideRoutingModule,
    OvideBackendApiModule,
    MaterialDesignModule,
    NgxChartsModule
  ],
  providers: [{provide: BASE_PATH, useValue: environment.API_BASE_PATH}],
  bootstrap: [OvideAppComponent]
})
export class OvideModule { }
