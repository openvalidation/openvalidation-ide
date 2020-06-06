import { Inject, Injectable, OnDestroy } from '@angular/core';
import { SchemaService } from '@ovide/core';
import { AttributeDto } from '@ovide/core/backend';
import {
  CloseAction,
  createConnection,
  ErrorAction,
  IConnection,
  MonacoLanguageClient,
  MonacoServices,
  Range
} from 'monaco-languageclient';
import { LanguageEnum, NotificationEnum } from 'ov-language-server-types';
import ReconnectingWebSocket from 'reconnecting-websocket';
import { Subject } from 'rxjs';
import { listen, MessageConnection } from 'vscode-ws-jsonrpc';
import { createTokenizationSupport } from '../monaco/TokensProvider';

@Injectable()
export class OVLanguageServerService implements OnDestroy {

  private languageServerUrl: string;

  private webSocket;
  private currentConnection: IConnection;

  private variables = new Subject<Array<IOvideVariable>>();
  private diagnostics = new Subject<Array<IOvideDiagnostic>>();

  public variables$ = this.variables.asObservable();
  public diagnostics$ = this.diagnostics.asObservable();

  private schemaAttributes: AttributeDto[];
  private editor;

  private initilization = new Subject<boolean>();

  constructor(
    @Inject('LANGUAGE_SERVER_URL') languageServerUrl,
    private schemaService: SchemaService
  ) {
    this.languageServerUrl = `${languageServerUrl}/ovLanguage`;
  }

  public initialize(editor) {
    if (this.initilization.isStopped) { return; }

    try {
      MonacoServices.get();
    } catch (e) {
      MonacoServices.install(editor);
    }

    this.editor = editor;
    this.webSocket = new ReconnectingWebSocket(this.languageServerUrl);
    listen({
      webSocket: this.webSocket,
      onConnection: (connection: MessageConnection) => {
        const languageClient = this.createLanguageClient(connection);
        const disposable = languageClient.start();
        connection.onClose(() => disposable.dispose());
        this.initilization.next(true);
        this.initilization.complete();
      }
    });
  }

  ngOnDestroy(): void {
    this.webSocket.close();
  }

  private createLanguageClient(connection: MessageConnection): MonacoLanguageClient {
    return new MonacoLanguageClient({
      name: `OV Client`,
      clientOptions: {
        // use a language id as a document selector
        documentSelector: ['ov'],
        // disable the default error handler
        errorHandler: {
          error: () => {
            return ErrorAction.Continue;
          },
          closed: () => CloseAction.DoNotRestart
        }
      },
      // create a language client connection from the JSON RPC connection on demand
      connectionProvider: {
        get: (errorHandler, closeHandler) => {
          this.currentConnection = createConnection(connection as any, errorHandler, closeHandler);

          this.addParsingResultNotificationListener();
          this.addSemanticHighlightingNotificationListener();
          this.addAliasesChangesListener();
          return Promise.resolve(this.currentConnection);
        }
      }
    });
  }

  /**
   * Adds listener to the notification ``openVALIDATION/parsingResult`` to set a few
   * language-configurations for the ov-language
   */
  private addParsingResultNotificationListener() {
    this.currentConnection.onNotification(
      NotificationEnum.ParsingResult,
      (params: any) => {
        const variables: Array<IOvideVariable> = params.variables;
        const errors: Array<IOvideDiagnostic> = params.diagnostics;
        this.variables.next(variables);
        this.diagnostics.next(errors);
      }
    );
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

        const fixedParameters = [];
        // Inject token values for syntax highlighting
        if (this.schemaAttributes !== undefined) {
          jsonParameter.forEach((value, index) => {
            if (value.pattern === 'variable.parameter.ov') {
              const foundAttribute = this.schemaAttributes.find(attribute => {
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
              fixedParameters.push(value);
            } else if (value.pattern === 'string.unquoted.ov') {
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
              fixedParameters.push(value);
            } else if (value.pattern === 'keyword.ov') {
              const keyword: string = this.editor.getModel().getValueInRange({
                startLineNumber: value.range.start.line + 1,
                endLineNumber: value.range.end.line + 1,
                startColumn: value.range.start.character,
                endColumn: value.range.end.character + 2
              });

              if (!(keyword.charAt(0) !== ' ' && value.range.start.character > 0
                || keyword.charAt(keyword.length - 1) !== ' ')) {
                fixedParameters.push(value);
              }
            } else {
              fixedParameters.push(value);
            }
          });
        }
        monaco.languages.setTokensProvider(
          'ov',
          createTokenizationSupport(fixedParameters)
        );
      }
    );
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

  public setSchema(schemaId: string) {
    this.initilization.subscribe({
      complete: () => {
        this.schemaService.getAllAttributesFromSchema(schemaId).subscribe(
          attributes => this.schemaAttributes = attributes
        );

        this.schemaService.exportSchema(schemaId).subscribe(
          schemaAsJson => this.currentConnection.sendNotification(NotificationEnum.SchemaChanged, {
            uri: this.editor.getModel().uri.toString(),
            schema: JSON.stringify(schemaAsJson)
          })
        );
      }
    });
  }

  public setCulture(culture: string) {
    this.initilization.subscribe({
      complete: () => this.currentConnection.sendNotification(NotificationEnum.CultureChanged, {
        uri: this.editor.getModel().uri.toString(),
        culture
      })
    });
  }

  public setOutputLanguage(language: LanguageEnum) {
    this.initilization.subscribe({
      complete: () => this.currentConnection.sendNotification(NotificationEnum.LanguageChanged, {
        uri: this.editor.getModel().uri.toString(),
        language
      })
    });
  }

}

export interface IOvideVariable {
  readonly name: string;
  readonly dataType: string;
}

export interface IOvideDiagnostic {
  readonly range: any;
  readonly message: string;
  readonly severity: number;
}
