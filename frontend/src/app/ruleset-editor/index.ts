import { InjectionToken } from '@angular/core';

export * from './ruleset-editor.component';

export const LANGUAGE_SERVER_URL = new InjectionToken<string>('languageServerUrl');
