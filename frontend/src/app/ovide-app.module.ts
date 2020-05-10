import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { CoreModule } from '@ovide/core';
import { SharedModule } from '@ovide/shared';
import { OvideAppRoutingModule } from '@ovide/ovide-app-routing.module';
import { OvideAppComponent } from '@ovide/ovide-app.component';
import { RulesetManagementModule } from './modules/ruleset-management';
import { RulesetIdeModule } from './modules/ruleset-ide';
import { RulesetEditorModule } from './modules/ruleset-editor';
import { RulesetTestsuiteModule } from './modules/ruleset-testsuite';



@NgModule({
  imports: [
    BrowserModule,
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
