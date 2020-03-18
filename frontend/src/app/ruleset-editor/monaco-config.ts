import { NgxMonacoEditorConfig } from 'ngx-monaco-editor';

export const MonacoConfig: NgxMonacoEditorConfig = {
  baseUrl: 'assets',
  defaultOptions: {scrollBeyondLastLine: false},
  onMonacoLoad: monacoOnLoad
};

export function monacoOnLoad() {
  // console.log((window as any).monaco);

  monaco.languages.register({
    id: 'ov',
    extensions: ['.ov'],
    aliases: ['OV', 'ov', 'openVALIDATION']
  });
}
