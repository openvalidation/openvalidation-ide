// tslint:disable: no-string-literal
export const environment = {
  production: true,
  API_BASE_PATH: window['env']['API_BASE_PATH'] || 'http://127.0.0.1:8080',
  LANGUAGE_SERVER_URL: window['env']['LANGUAGE_SERVER_URL'] || 'ws://127.0.0.1:3010'
};
