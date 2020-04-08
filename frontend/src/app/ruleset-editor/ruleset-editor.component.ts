import { animate, query, stagger, style, transition, trigger } from '@angular/animations';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';
import { ErrorHandlerService } from '@ovide/services/error-handler.service';
import { IOvideDiagnostic, IOvideVariable, OVLanguageServerService } from '@ovide/services/ov-language-server.service';
import { SchemaService } from '@ovide/services/schema.service';
import { ThemeService } from '@ovide/services/theme.service';
import { LanguageEnum } from 'ov-language-server-types';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged, filter, map, retry, switchMap, take } from 'rxjs/operators';

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss'],
  animations: [
    trigger('editorAnimation', [
      transition(':enter', [
        style({ transform: 'scale(0.9)', opacity: 0.2 }),
        animate('.4s ease-in-out')
      ])
    ]),
    trigger('variableAnimation', [
      transition(':enter', [
        query('*', [
          style({ transform: 'scale(0.5)', opacity: 0 }),
          stagger(30, [
            animate('.2s ease-out')
          ]),
        ], { optional: true })
      ])
    ])
  ],
  providers: [OVLanguageServerService]
})
export class RulesetEditorComponent implements OnInit, OnDestroy {
  private lastSavedRules: string;
  private savingRulesInProgress$ = new BehaviorSubject<boolean>(false);

  public variables: IOvideVariable[];
  public editorErrors: IOvideDiagnostic[];

  editorOptions = {
    theme: 'vs-dark',
    language: 'ov',
    fontFamily: 'Source Code Pro',
    minimap: {
      enabled: false
    },
    lineNumbers: false
  };

  ruleset: RulesetDto;
  editorText: FormControl;
  rulesetName: FormControl;
  rulesetDescription: FormControl;

  editorInitDone = false;
  private editor;
  private subscriptions = new Subscription();

  constructor(
    private route: ActivatedRoute,
    private rulesetsBackendService: RulesetsBackendService,
    private schemaService: SchemaService,
    public themeService: ThemeService,
    private changeDetectorRef: ChangeDetectorRef,
    private errorHandlerService: ErrorHandlerService,
    private ovLanguageServerService: OVLanguageServerService
  ) {
  }

  private static readStyleProperty(name: string): string {
    const bodyStyles = window.getComputedStyle(document.documentElement);
    return bodyStyles.getPropertyValue('--' + name).trim();
  }

  ngOnInit(): void {
    this.editorText = new FormControl();
    this.rulesetName = new FormControl('', [Validators.required]);
    this.rulesetDescription = new FormControl();

    this.subscriptions.add(this.route.paramMap.pipe(
      map(params => params.get('id')),
      switchMap(id => this.rulesetsBackendService.getRuleset(id))
    ).subscribe(
      ruleset => this.openRuleset(ruleset),
      () => this.errorHandlerService.createError('Error fetching ruleset.')
    ));

    this.subscriptions.add(this.editorText.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      filter(rules => rules !== this.lastSavedRules)
    ).subscribe(
      rules => this.saveRules(this.ruleset.rulesetId, rules),
      () => this.errorHandlerService.createError('Error saving ruleset.')
    ));

    this.subscriptions.add(this.rulesetName.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      filter(name => this.rulesetName.valid && this.ruleset.name !== name)
    ).subscribe(
      name => this.saveName(this.ruleset.rulesetId, name)
    ));

    this.subscriptions.add(this.rulesetDescription.valueChanges.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      filter(description => this.ruleset.name !== description)
    ).subscribe(
      description => this.saveDescription(this.ruleset.rulesetId, description)
    ));

    this.subscriptions.add(
      this.schemaService.schemaId$.subscribe(
        schemaId => this.ovLanguageServerService.setSchema(schemaId)
      )
    );

    this.subscriptions.add(
      this.ovLanguageServerService.variables$.subscribe(
        variables => {
          this.variables = variables;
          this.changeDetectorRef.detectChanges();
        }
      )
    );

    this.subscriptions.add(
      this.ovLanguageServerService.diagnostics$.subscribe(
        diagnostics => {
          this.editorErrors = diagnostics;
          this.changeDetectorRef.detectChanges();
        }
      )
    );

    this.subscriptions.add(
      this.themeService.darkThemeActive$.subscribe((isDark) => {
        this.updateTheme(isDark);
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
    this.closeRuleset().subscribe();
  }

  private openRuleset(ruleset: RulesetDto) {
    this.closeRuleset().subscribe(
      () => {
        this.ruleset = ruleset;
        this.lastSavedRules = ruleset.rules;
        this.editorText.setValue(ruleset.rules);
        this.rulesetName.setValue(ruleset.name);
        this.rulesetDescription.setValue(ruleset.description);
        this.schemaService.setSchema(ruleset.schemaId);
      }
    );
  }

  private closeRuleset(): Observable<any> {
    return new Observable(
      observer => {
        const complete = () => {
          observer.next();
          observer.complete();
        };
        if (this.lastSavedRules === undefined) {
          complete();
          return;
        }
        const savingDone = this.savingRulesInProgress$.pipe(filter(x => x === false), take(1));
        savingDone.subscribe(
          () => {
            if (this.lastSavedRules !== this.editorText.value) {
              this.saveRules(this.ruleset.rulesetId, this.editorText.value);
              savingDone.subscribe(() => complete());
            } else {
              complete();
            }
          }
        );
      }
    );
  }

  private saveRules(rulesetId: string, rules: string) {
    this.savingRulesInProgress$.next(true);
    this.rulesetsBackendService.updateRuleset(rulesetId, { rules }).pipe(retry(5))
      .subscribe(
        success => this.lastSavedRules = rules,
        () => this.errorHandlerService.createError('Error saving ruleset.'),
        () => this.savingRulesInProgress$.next(false)
      );
  }

  private saveName(rulesetId: string, name: string) {
    this.rulesetsBackendService.updateRuleset(rulesetId, { name }).pipe(retry(5))
      .subscribe(
        success => this.ruleset.name = name,
        () => this.errorHandlerService.createError('Error saving ruleset name.')
      );
  }

  private saveDescription(rulesetId: string, description: string) {
    this.rulesetsBackendService.updateRuleset(rulesetId, { description }).pipe(retry(5))
      .subscribe(
        success => this.ruleset.description = description,
        () => this.errorHandlerService.createError('Error saving ruleset description.')
      );
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

  monacoInit(editor) {
    this.editor = editor;
    this.editorInitDone = true;
    this.changeDetectorRef.detectChanges();

    this.themeService.darkThemeActive$.pipe(take(1)).subscribe((isDark) => {
        this.updateTheme(isDark);
      }
    );

    monaco.editor.setTheme('ovide-theme');
    this.ovLanguageServerService.initialize(editor);
    this.ovLanguageServerService.setCulture('en');
    this.ovLanguageServerService.setOutputLanguage(LanguageEnum.JavaScript);
  }

}
