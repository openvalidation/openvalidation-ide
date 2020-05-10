import { Component, forwardRef, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormControl, NG_VALUE_ACCESSOR } from '@angular/forms';
import { ThemeService } from '@ovide/core';
import { LanguageEnum } from 'ov-language-server-types';
import { Subscription } from 'rxjs';
import { OVLanguageServerService } from '../../services/ov-language-server.service';

const CUSTOM_VALUE_ACCESSOR: any = {
  provide: NG_VALUE_ACCESSOR,
  useExisting: forwardRef(() => RulesetEditorComponent),
  multi: true
};

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss'],
  providers: [CUSTOM_VALUE_ACCESSOR]
})
export class RulesetEditorComponent implements OnInit, OnDestroy, ControlValueAccessor {

  constructor(
    private ovLanguageServerService: OVLanguageServerService,
    private themeService: ThemeService
  ) { }

  private editor;
  private subscriptions: Subscription;

  public editorText: FormControl;
  public editorInitDone: boolean;
  public editorOptions = {
    theme: 'vs-dark',
    language: 'ov',
    fontFamily: 'Source Code Pro',
    minimap: {
      enabled: false
    },
    lineNumbers: false,
    readOnly: false
  };

  private static readStyleProperty(name: string): string {
    const bodyStyles = window.getComputedStyle(document.documentElement);
    return bodyStyles.getPropertyValue('--' + name).trim();
  }

  ngOnInit(): void {
    this.subscriptions = new Subscription();
    this.editorInitDone = false;
    this.editorText = new FormControl();

    this.subscriptions.add(this.editorText.valueChanges.subscribe(
      value => this.onChanged(value)
    ));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  private onChanged: any = () => {};
  private onTouched: any = () => {};

  writeValue(obj: any): void {
    if (typeof obj === 'string' || obj instanceof String) {
      this.editorText.setValue(obj, { emitEvent: false, onlySelf: true });
    }
  }

  registerOnChange(fn: any): void {
    this.onChanged = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    this.editor?.updateOptions({readOnly: isDisabled});
    this.editorOptions.readOnly = isDisabled;
  }

  monacoInit(editor) {
    this.editor = editor;
    this.editorInitDone = true;

    this.subscriptions.add(
      this.themeService.darkThemeActive$.subscribe((isDark) => {
        this.updateTheme(isDark);
      })
    );

    monaco.editor.setTheme('ovide-theme');
    this.ovLanguageServerService.initialize(editor);
    this.ovLanguageServerService.setCulture('en');
    this.ovLanguageServerService.setOutputLanguage(LanguageEnum.JavaScript);
  }

  private updateTheme(isDark: boolean) {
    if (this.editor !== undefined) {
      monaco.editor.defineTheme('ovide-theme', {
        base: isDark ? 'vs-dark' : 'vs',
        inherit: true,
        rules: [
          { token: 'keyword.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-keyword') },
          { token: 'string.unquoted.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-type-string') },
          { token: 'string.error.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-error') },
          { token: 'variable.parameter.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-variable') },
          { token: 'variable.number.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-type-number') },
          { token: 'variable.text.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-type-string') },
          { token: 'variable.boolean.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-type-boolean') },
          { token: 'constant.numeric.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-type-number') },
          { token: 'constant.boolean.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-type-boolean') },
          { token: 'comment.block.ov', foreground: RulesetEditorComponent.readStyleProperty('editor-comment') }
        ],
        colors: {
          'editor.background': RulesetEditorComponent.readStyleProperty('editor-background'),
          'editor.lineHighlightBorder': RulesetEditorComponent.readStyleProperty('editor-lineHighlightBorder'),
          'editor.selectionBackground': RulesetEditorComponent.readStyleProperty('editor-selectionBackground'),
        }
      });
      monaco.editor.setTheme('ovide-theme');
    }
  }

}
