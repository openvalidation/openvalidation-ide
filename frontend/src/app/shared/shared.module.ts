import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { OvideLogoComponent } from './components/ovide-logo/ovide-logo.component';
import { EllipsisDirective } from './directives/ellipsis/ellipsis.directive';
import { MaterialDesignModule } from './material-design.module';



@NgModule({
  imports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialDesignModule
  ],
  declarations: [
    OvideLogoComponent,
    EllipsisDirective
  ],
  exports: [
    CommonModule,
    RouterModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MaterialDesignModule,
    OvideLogoComponent,
    EllipsisDirective
  ]
})
export class SharedModule { }
