import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { debounceTime, distinctUntilChanged, filter, map, retry, switchMap, take, tap } from 'rxjs/operators';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
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
import { FormControl } from '@angular/forms';

const ReconnectingWebSocket = require('reconnecting-websocket');

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss']
})
export class RulesetEditorComponent implements OnInit, OnDestroy {


  private lastSavedRules: string;
  private savingRulesInProgress$ = new BehaviorSubject<boolean>(false);

  private languageId = 'ov';
  variables: Array<string>;
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
    private themeService: ThemeService,
    @Inject('LANGUAGE_SERVER_URL') languageServerUrl
  ) {
    this.languageServerUrl = languageServerUrl;
  }

  ngOnInit(): void {
    this.editorText = new FormControl();

    this.subscriptions.add(this.route.paramMap.pipe(
      map(params => params.get('id')),
      switchMap(id => this.rulesetsBackendService.getRuleset(id))
    ).subscribe(
      ruleset => this.openRuleset(ruleset)
    ));

    this.subscriptions.add(this.editorText.valueChanges.pipe(
      tap(rules => this.updateVariables(rules)),
      debounceTime(500),
      distinctUntilChanged(),
      filter(rules => rules !== this.lastSavedRules)
    ).subscribe(
      rules => this.saveRules(this.ruleset.rulesetId, rules)
    ));

    this.subscriptions.add(
      this.schemaService.schemaId$.pipe(
        switchMap(schemaId => this.schemaService.exportSchema(schemaId)),
        tap(schema => {
          this.schemaValue = schema;
          if (this.editor !== undefined && this.currentConnection !== undefined) {
            this.sendSchemaChangedNotification();
          }
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
    this.rulesetsBackendService.updateRuleset(rulesetId, {rules}).pipe(retry(5))
    .subscribe(
      success => {
        this.lastSavedRules = rules;
        this.savingRulesInProgress$.next(false);
      },
      error => {
        // TODO show error
        this.savingRulesInProgress$.next(false);
      }
    );
  }

  private updateTheme(isDark: boolean) {
    if (this.editor !== undefined) {
      monaco.editor.defineTheme('ovide-theme', {
        base: isDark ? 'vs-dark' : "vs",
        inherit: true,
        rules: [
          { token: 'keyword.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-keyword').trim() },
          { token: 'string.unquoted.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-type-string').trim() },
          { token: 'string.error.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-error').trim() },
          { token: 'variable.parameter.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-variable').trim() },
          { token: 'variable.number.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-type-number').trim() },
          { token: 'variable.text.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-type-string').trim() },
          { token: 'variable.boolean.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-type-boolean').trim() },
          { token: 'constant.numeric.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-type-number').trim() },
          { token: 'constant.boolean.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-type-boolean').trim() },
          { token: 'comment.block.ov', foreground: getComputedStyle(document.documentElement).getPropertyValue('--editor-comment').trim() }
        ],
        colors: {
          'editor.background': getComputedStyle(document.documentElement).getPropertyValue('--editor-background').trim(),
          'editor.lineHighlightBorder': getComputedStyle(document.documentElement).getPropertyValue('--editor-lineHighlightBorder').trim(),
          'editor.selectionBackground': getComputedStyle(document.documentElement).getPropertyValue('--editor-selectionBackground').trim(),
        }
      });
      monaco.editor.setTheme('ovide-theme');
    }
  }

  monacoInit(editor) {
    this.editor = editor;

    this.updateTheme(true);
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
          error: () => ErrorAction.Continue,
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
        if(this.attributes !== undefined) {
          jsonParameter.forEach(value => {
            if (value.pattern === 'variable.parameter.ov') {
              const foundAttribute = this.attributes.find(attribute => {
                return attribute.name === this.editor.getModel().getValueInRange({
                  startLineNumber: value.range.start.line + 1,
                  endLineNumber: value.range.end.line + 1,
                  startColumn: value.range.start.character + 1,
                  endColumn: value.range.end.character + 1
                });
              });
              if (foundAttribute !== undefined) {
                value.pattern = 'variable.' + foundAttribute.attributeType.toLowerCase() + '.ov';
              }
            }
            if (value.pattern === 'string.unquoted.ov') {
              const thenInRange = this.editor.getModel().getValueInRange({
                startLineNumber: value.range.start.line + 1,
                endLineNumber: value.range.end.line + 1,
                startColumn: 1,
                endColumn: value.range.start.character
              });

              if (thenInRange.toLowerCase() === 'then') {
                value.pattern = 'string.error.ov';
              }

              const trueOrFalseInRange = this.editor.getModel().getValueInRange({
                startLineNumber: value.range.start.line + 1,
                endLineNumber: value.range.end.line + 1,
                startColumn: value.range.start.character + 1,
                endColumn: value.range.end.character + 1
              });

              if(trueOrFalseInRange.toLowerCase() === 'true'
                || trueOrFalseInRange.toLowerCase() === 'false'
                || trueOrFalseInRange.toLowerCase() === 'yes'
                || trueOrFalseInRange.toLowerCase() === 'no') {
                value.pattern = 'constant.boolean.ov';
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

  private sendLanguageConfiguration() {
    const textdocumentUri = this.editor.getModel().uri.toString();

    this.currentConnection.sendNotification(NotificationEnum.LanguageChanged, {
      language: LanguageEnum.JavaScript,
      uri: textdocumentUri
    });
  }

  updateVariables(code: string) {
    // find matches
    const globalRegex = /[ \n]AS .*/gi;
    // find variable names
    const localRegex = /[ \n]AS (.*)/i;
    const results = code.match(globalRegex);
    this.variables = [];
    if (Array.isArray(results)) {
      for (const result of results) {
        this.variables.push(result.match(localRegex)[1]);
      }
    }
  }
}
