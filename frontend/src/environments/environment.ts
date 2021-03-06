// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// tslint:disable: no-string-literal

export const environment = {
  production: false,
  API_BASE_PATH: window['env']['API_BASE_PATH'] || 'http://127.0.0.1:8080',
  LANGUAGE_SERVER_URL: window['env']['LANGUAGE_SERVER_URL'] || 'ws://127.0.0.1:3010'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
