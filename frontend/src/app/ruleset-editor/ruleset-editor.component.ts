import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs/operators';
import { RulesetsService, RulesetDto } from '@ovide/backend';
import { Observable } from 'rxjs';
import { ThemeService } from '@ovide/services/theme.service';
import { MonacoLanguageClient, CloseAction, ErrorAction, MonacoServices, createConnection } from 'monaco-languageclient';
import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
const ReconnectingWebSocket = require('reconnecting-websocket');

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss']
})
export class RulesetEditorComponent implements OnInit {
  private languageId = 'ov';
  editorOptions = {
    theme: 'vs-dark',
    language: this.languageId,
    minimap: {
      enabled: false
    },
    lineNumbers: false
  };
  code = 'Der Text muss Validaria sein';
  ruleset$: Observable<RulesetDto>;
  private editor;

  constructor(
    private route: ActivatedRoute,
    private rulesetsService: RulesetsService,
    private themeService: ThemeService,
  ) {
  }

  ngOnInit(): void {
    this.ruleset$ = this.route.paramMap
      .pipe(
        map(params => params.get('id')),
        switchMap(id => this.rulesetsService.getRuleset(id)),
      );

    this.themeService.darkThemeActive$.subscribe((isDark) => {
      if (this.editor !== undefined) {
        this.editor.setTheme(isDark ? 'vs-dark' : 'vs');
      }
    });
  }

  monacoInit(editor) {
    // install Monaco language client services
    MonacoServices.install(editor);
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
    return 'ws://localhost:3010/ovLanguage';
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
          return Promise.resolve(createConnection(connection as any, errorHandler, closeHandler));
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
}
