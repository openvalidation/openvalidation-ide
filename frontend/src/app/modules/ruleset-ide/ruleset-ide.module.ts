import { NgModule } from '@angular/core';
import { SharedModule } from '@ovide/shared';
import { RulesetIdeComponent } from './components/ruleset-ide/ruleset-ide.component';
import { SchemaEditorComponent } from './components/schema-editor/schema-editor.component';
import { SchemaAttributeDialogComponent } from './components/schema-attribute-dialog/schema-attribute-dialog.component';
import { RulesetEditorModule } from '../ruleset-editor';



@NgModule({
  declarations: [
    RulesetIdeComponent,
    SchemaEditorComponent,
    SchemaAttributeDialogComponent
  ],
  imports: [
    SharedModule,
    RulesetEditorModule
  ],
  exports: [
    RulesetIdeComponent
  ]
})
export class RulesetIdeModule { }
