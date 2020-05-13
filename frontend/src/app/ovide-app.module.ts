import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { CoreModule } from '@ovide/core';
import { OvideAppRoutingModule } from '@ovide/ovide-app-routing.module';
import { OvideAppComponent } from '@ovide/ovide-app.component';
import { SharedModule } from '@ovide/shared';
import { RulesetEditorModule } from './modules/ruleset-editor';
import { RulesetIdeModule } from './modules/ruleset-ide';
import { RulesetManagementModule } from './modules/ruleset-management';
import { RulesetTestsuiteModule } from './modules/ruleset-testsuite';



@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    CoreModule,
    SharedModule,
    RulesetManagementModule,
    RulesetIdeModule,
    RulesetEditorModule,
    RulesetTestsuiteModule,
    OvideAppRoutingModule,
  ],
  declarations: [OvideAppComponent],
  bootstrap: [OvideAppComponent]
})
export class OvideAppModule { }
