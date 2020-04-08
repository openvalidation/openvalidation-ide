import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
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

import { BASE_PATH, OvideBackendApiModule } from '@ovide/backend';
import { RulesetTestsuiteComponent } from '@ovide/ruleset-testsuite';
import { SchemaAttributeDialogComponent } from '@ovide/schema-attribute-dialog';
import { environment } from 'environments/environment';
import { EllipsisDirective } from './directives/ellipsis.directive';
import { MonacoEditorModule } from 'ngx-monaco-editor';

import { monacoEditorConfig } from '@ovide/ruleset-editor/monaco-editor-config';

import { OvideLogoComponent } from './ovide-logo/ovide-logo.component';
import { GaugeChartComponent } from './gauge-chart/gauge-chart.component';

// import { ErrorHandlerService } from '@ovide/services/error-handler.service';

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
    OvideLogoComponent,
    GaugeChartComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    OvideRoutingModule,
    OvideBackendApiModule,
    MaterialDesignModule,
    NgxChartsModule,
    MonacoEditorModule.forRoot(monacoEditorConfig),
    FormsModule
  ],
  providers: [
    {provide: BASE_PATH, useValue: environment.API_BASE_PATH},
    {provide: 'LANGUAGE_SERVER_URL', useValue: environment.LANGUAGE_SERVER_URL},
    // {provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerService, multi: true},
  ],
  bootstrap: [OvideAppComponent]
})
export class OvideModule { }
