import { NgModule } from '@angular/core';
import { SharedModule } from '@ovide/shared';
import { environment } from 'environments/environment';
import { RulesetEditorComponent } from './components/ruleset-editor/ruleset-editor.component';
import { MonacoEditorModule } from 'ngx-monaco-editor';
import { monacoEditorConfig } from './monaco/monaco-editor-config';



@NgModule({
  declarations: [
    RulesetEditorComponent
  ],
  imports: [
    SharedModule,
    MonacoEditorModule.forRoot(monacoEditorConfig)
  ],
  exports: [
    RulesetEditorComponent
  ],
  providers: [
    {provide: 'LANGUAGE_SERVER_URL', useValue: environment.LANGUAGE_SERVER_URL}
  ]
})
export class RulesetEditorModule { }
