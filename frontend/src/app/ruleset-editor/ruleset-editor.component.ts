import { Component, Inject, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  debounceTime,
  distinctUntilChanged,
  filter,
  map,
  retry,
  switchMap,
  take,
  tap,
  catchError
} from 'rxjs/operators';
import { BehaviorSubject, Observable, Subscription, Subject, of } from 'rxjs';
import { ThemeService } from '@ovide/services/theme.service';
import {
  CloseAction,
  createConnection,
  ErrorAction,
  IConnection,
  MonacoLanguageClient,
  MonacoServices,
  Range
} from 'monaco-languageclient';
import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
import { LanguageEnum, NotificationEnum } from 'ov-language-server-types';
import { createTokenizationSupport } from '@ovide/monaco-additions/syntax-highlighting/TokensProvider';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';
import { SchemaService } from '@ovide/services/schema.service';
import { FormControl, Validators } from '@angular/forms';
import { trigger, transition, style, animate, query, stagger } from '@angular/animations';
import { ErrorHandlerService } from '@ovide/services/error-handler.service';

const ReconnectingWebSocket = require('reconnecting-websocket');

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
  ]
})
export class RulesetEditorComponent implements OnInit, OnDestroy {
  private lastSavedRules: string;
  private savingRulesInProgress$ = new BehaviorSubject<boolean>(false);

  private languageId = 'ov';
  public variables$ = new Subject<Array<IVariable>>();
  public editorErrors$ = new Subject<Array<IError>>();

  editorOptions = {
    theme: 'vs-dark',
    language: this.languageId,
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
  private currentConnection: IConnection;
  private subscriptions = new Subscription();
  private schemaValue;
  private languageServerUrl: string;
  private webSocket;
  private attributes;

  constructor(
    private route: ActivatedRoute,
    private rulesetsBackendService: RulesetsBackendService,
    private schemaService: SchemaService,
    public themeService: ThemeService,
    @Inject('LANGUAGE_SERVER_URL') languageServerUrl,
    private changeDetectorRef: ChangeDetectorRef,
    private errorHandlerService: ErrorHandlerService
  ) {
    this.languageServerUrl = languageServerUrl;
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
      this.schemaService.schemaId$.pipe(
        switchMap(schemaId => this.schemaService.exportSchema(schemaId)),
        tap(schema => {
          this.schemaValue = schema;
          if (this.editor !== undefined && this.currentConnection !== undefined) {
            this.sendSchemaChangedNotification();
          }
        }),
        catchError(() => {
          this.errorHandlerService.createError('Error fetching schema.');
          return of();
        })
      ).subscribe()
    );

    this.subscriptions.add(
      this.schemaService.schemaId$.pipe(
        switchMap(schemaId => this.schemaService.getAllAttributesFromSchema(schemaId)),
        tap(attributes => {
          this.attributes = attributes;
        })
      ).subscribe()
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
    this.webSocket.close();
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

    // install Monaco language client services
    try {
      MonacoServices.get();
    } catch (e) {
      MonacoServices.install(editor);
    }
    // create the web socket
    const url = this.createUrl();
    const webSocket = this.createWebSocket(url);
    this.webSocket = webSocket;
    // listen when the web socket is opened
    listen({
      webSocket,
      onConnection: (connection: MessageConnection) => {
        // create and start the language client
        const languageClient = this.createLanguageClient(connection);
        const disposable = languageClient.start();
        connection.onClose(() => disposable.dispose());
      }
    });
  }

  public createUrl(): string {
    return `${this.languageServerUrl}/ovLanguage`;
  }

  public createLanguageClient(connection: MessageConnection): MonacoLanguageClient {
    return new MonacoLanguageClient({
      name: `${this.languageId.toUpperCase()} Client`,
      clientOptions: {
        // use a language id as a document selector
        documentSelector: [this.languageId],
        // disable the default error handler
        errorHandler: {
          error: () => {
            // this.errorHandlerService.createError('Error connecting to required service.');
            return ErrorAction.Continue;
          },
          closed: () => CloseAction.DoNotRestart
        }
      },
      // create a language client connection from the JSON RPC connection on demand
      connectionProvider: {
        get: (errorHandler, closeHandler) => {
          this.currentConnection = createConnection(connection as any, errorHandler, closeHandler);

          // Informs the server about the initialized schema
          this.sendSchemaChangedNotification();
          this.sendCultureConfiguration();
          this.sendLanguageConfiguration();

          this.addParsingResultNotificationListener();
          this.addSemanticHighlightingNotificationListener();
          this.addAliasesChangesListener();
          return Promise.resolve(this.currentConnection);
        }
      }
    });
  }

  public createWebSocket(socketUrl: string): any {
    const socketOptions = {
      maxReconnectionDelay: 10000,
      minReconnectionDelay: 1000,
      reconnectionDelayGrowFactor: 1.3,
      connectionTimeout: 10000,
      maxRetries: Infinity,
      debug: false
    };
    return new ReconnectingWebSocket.default(socketUrl, [], socketOptions);
  }

  private sendSchemaChangedNotification() {
    const textdocumentUri = this.editor.getModel().uri.toString();
    this.currentConnection.sendNotification(NotificationEnum.SchemaChanged, {
      schema: JSON.stringify(this.schemaValue),
      uri: textdocumentUri,
    });
  }

  /**
   * Adds listener to the notification ``textDocument/semanticHighlighting`` to set a new
   * tokenizer for syntax-highlighting
   */
  private addSemanticHighlightingNotificationListener() {
    // Handler for semantic-highlighting
    this.currentConnection.onNotification(
      NotificationEnum.SemanticHighlighting,
      (params: any) => {
        const jsonParameter = JSON.parse(params) as {
          range: Range;
          pattern: string;
        }[];

        // Inject token values for syntax highlighting
        if (this.attributes !== undefined) {
          jsonParameter.forEach((value, index) => {
            if (value.pattern === 'variable.parameter.ov') {
              const foundAttribute = this.attributes.find(attribute => {
                return attribute.name.toLowerCase() === this.editor.getModel().getValueInRange({
                  startLineNumber: value.range.start.line + 1,
                  endLineNumber: value.range.end.line + 1,
                  startColumn: value.range.start.character + 1,
                  endColumn: value.range.end.character + 1
                }).toLowerCase();
              });
              if (foundAttribute !== undefined) {
                value.pattern = 'variable.' + foundAttribute.attributeType.toLowerCase() + '.ov';
              }
            }
            if (value.pattern === 'string.unquoted.ov') {
              const thenInRange = this.editor.getModel().getValueInRange({
                startLineNumber: value.range.start.line + 1,
                endLineNumber: value.range.start.line + 1,
                startColumn: 1,
                endColumn: value.range.start.character
              });

              if (thenInRange.trim().toLowerCase() === 'then') {
                value.pattern = 'string.error.ov';
              }

              const trueOrFalseInRange = this.editor.getModel().getValueInRange({
                startLineNumber: value.range.start.line + 1,
                endLineNumber: value.range.end.line + 1,
                startColumn: value.range.start.character + 1,
                endColumn: value.range.end.character + 1
              });

              if (trueOrFalseInRange.toLowerCase() === 'true'
                || trueOrFalseInRange.toLowerCase() === 'false'
                || trueOrFalseInRange.toLowerCase() === 'yes'
                || trueOrFalseInRange.toLowerCase() === 'no') {
                value.pattern = 'constant.boolean.ov';
              }

            }
            if (value.pattern === 'keyword.ov') {
              const keyword: string = this.editor.getModel().getValueInRange({
                startLineNumber: value.range.start.line + 1,
                endLineNumber: value.range.end.line + 1,
                startColumn: value.range.start.character,
                endColumn: value.range.end.character + 2
              });

              if (keyword.charAt(0) !== ' ' && value.range.start.character > 0
                || keyword.charAt(keyword.length - 1) !== ' ') {
                jsonParameter.splice(index, 1);
              }
            }
          });
        }
        monaco.languages.setTokensProvider(
          'ov',
          createTokenizationSupport(jsonParameter)
        );
      }
    );
  }

  /**
   * Sends the client the culture and language to the server
   */
  private sendCultureConfiguration() {
    const textdocumentUri = this.editor.getModel().uri.toString();

    this.currentConnection.sendNotification(NotificationEnum.CultureChanged, {
      culture: 'en',
      uri: textdocumentUri
    });
  }

  /**
   * Adds listener to the notification ``textDocument/aliasesChanges`` to set a few
   * language-configurations for the ov-language
   */
  private addAliasesChangesListener() {
    // Handler for semantic-highlighting
    this.currentConnection.onNotification(
      NotificationEnum.CommentKeywordChanged,
      (params: string) => {
        monaco.languages.setLanguageConfiguration('ov', {
          comments: {
            lineComment: params as string
          }
        });
      }
    );
  }

  /**
   * Adds listener to the notification ``openVALIDATION/parsingResult`` to set a few
   * language-configurations for the ov-language
   */
  private addParsingResultNotificationListener() {
    this.currentConnection.onNotification(
      NotificationEnum.ParsingResult,
      (params: any) => {
        const variables: Array<IVariable> = params.variables;
        const errors: Array<IError> = params.diagnostics;
        this.variables$.next(variables);
        this.editorErrors$.next(errors);
        this.changeDetectorRef.detectChanges();
      }
    );
  }

  private sendLanguageConfiguration() {
    const textdocumentUri = this.editor.getModel().uri.toString();

    this.currentConnection.sendNotification(NotificationEnum.LanguageChanged, {
      language: LanguageEnum.JavaScript,
      uri: textdocumentUri
    });
  }
}

export interface IVariable {
  readonly name: string;
  readonly dataType: string;
}

export interface IError {
  readonly range: any;
  readonly message: string;
  readonly severity: number;
}
