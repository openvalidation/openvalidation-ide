import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap, tap } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
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
import { environment } from 'environments/environment';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';
import { SchemaService } from '@ovide/services/schema.service';

const ReconnectingWebSocket = require('reconnecting-websocket');

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss']
})
export class RulesetEditorComponent implements OnInit, OnDestroy {
  private languageId = 'ov';
  variables: Array<string>;
  editorOptions = {
    theme: 'vs-dark',
    language: this.languageId,
    minimap: {
      enabled: false
    },
    lineNumbers: false
  };
  code = 'IF text IS NOT humbug\n' +
    'THEN Das ist kein humbug\n' +
    '\n' +
    'age LOWER THAN 18 as underage\n' +
    '\n' +
    'IF underage\n' +
    'THEN too young';
  ruleset$: Observable<RulesetDto>;
  private editor;
  private currentConnection: IConnection;
  private subscriptions: Subscription;
  private schemaValue;

  constructor(
    private route: ActivatedRoute,
    private rulesetsBackendService: RulesetsBackendService,
    private schemaService: SchemaService,
    private themeService: ThemeService,
  ) {
  }

  ngOnInit(): void {
    this.subscriptions = new Subscription();

    this.ruleset$ = this.route.paramMap
      .pipe(
        map(params => params.get('id')),
        switchMap(id => this.rulesetsBackendService.getRuleset(id)),
        tap(ruleset => this.schemaService.setSchema(ruleset.schemaId))
      );

    this.subscriptions.add(
      this.schemaService.schemaId$.pipe(
        switchMap(schemaId => this.schemaService.exportSchema(schemaId)),
        tap(schema => {
          this.schemaValue = schema;
          if (this.editor !== undefined) {
            this.sendSchemaChangedNotification();
          }
        })
      ).subscribe()
    );

    this.subscriptions.add(
      this.themeService.darkThemeActive$.subscribe((isDark) => {
        const nextTheme = isDark ? 'vs-dark' : 'vs';
        if (this.editor !== undefined) {
          monaco.editor.setTheme(nextTheme);
        } else {
          this.editorOptions.theme = nextTheme;
        }
      })
    );

    this.updateVariables();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  monacoInit(editor) {
    this.editor = editor;
    // install Monaco language client services
    try {
      MonacoServices.get();
    } catch (e) {
      MonacoServices.install(editor);
    }
    // create the web socket
    const url = this.createUrl();
    const webSocket = this.createWebSocket(url);
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
    return `${environment.LANGUAGE_SERVER_URL}/ovLanguage`;
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

  public createWebSocket(socketUrl: string): WebSocket {
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

  updateVariables() {
    // find matches
    const globalRegex = /[ \n]AS .*/gi;
    // find variable names
    const localRegex = /[ \n]AS (.*)/i;
    const results = this.code.match(globalRegex);
    this.variables = [];
    for (const result of results) {
      this.variables.push(result.match(localRegex)[1]);
    }
  }
}
