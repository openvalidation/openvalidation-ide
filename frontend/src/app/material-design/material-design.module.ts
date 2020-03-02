import { NgModule } from '@angular/core';

import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatChipsModule} from '@angular/material/chips';


@NgModule({
    exports: [
        MatInputModule,
        MatButtonModule,
        MatChipsModule
    ]
})
export class MaterialDesignModule { }