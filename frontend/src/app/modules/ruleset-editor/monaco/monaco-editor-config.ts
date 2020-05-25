import { NgxMonacoEditorConfig } from 'ngx-monaco-editor';

export const monacoEditorConfig: NgxMonacoEditorConfig = {
  baseUrl: 'assets',
  defaultOptions: {scrollBeyondLastLine: false},
  onMonacoLoad: monacoOnLoad
};

export function monacoOnLoad() {
  monaco.languages.register({
    id: 'ov',
    extensions: ['.ov'],
    aliases: ['OV', 'ov', 'openVALIDATION']
  });
}
