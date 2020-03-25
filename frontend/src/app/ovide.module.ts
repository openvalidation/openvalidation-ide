import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialDesignModule } from '@ovide/material-design';
import { OvideAppComponent } from '@ovide/ovide-app';
import { OvideRoutingModule } from '@ovide/routing';
import { RulesetCreatorComponent } from '@ovide/ruleset-creator';
import { LANGUAGE_SERVER_URL, RulesetEditorComponent } from '@ovide/ruleset-editor';
import { RulesetsOverviewComponent } from '@ovide/rulesets-overview';
import { SchemaEditorComponent } from '@ovide/schema-editor';
import { NgxChartsModule } from '@swimlane/ngx-charts';

import { BASE_PATH, OvideBackendApiModule } from '@ovide/backend';
import { RulesetTestsuiteComponent } from '@ovide/ruleset-testsuite';
import { SchemaAttributeDialogComponent } from '@ovide/schema-attribute-dialog';
import { environment } from 'environments/environment';
import { EllipsisDirective } from './directives/ellipsis.directive';

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
    EllipsisDirective,
    DesignTestComponent
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
  providers: [
    {provide: BASE_PATH, useValue: environment.API_BASE_PATH},
    {provide: LANGUAGE_SERVER_URL, useValue: environment.LANGUAGE_SERVER_URL}
  ],
  bootstrap: [OvideAppComponent]
})
export class OvideModule { }
