import { NgModule, Optional, SkipSelf } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { OvideBackendApiModule, BASE_PATH } from './backend';
import { environment } from 'environments/environment';



@NgModule({
  imports: [
    HttpClientModule,
    OvideBackendApiModule
  ],
  providers: [
    {provide: BASE_PATH, useValue: environment.API_BASE_PATH}
  ]
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error(`${parentModule} has already been loaded. Import Core module in the AppModule only.`);
    }
  }
}
