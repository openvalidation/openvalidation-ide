import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {

  private errorQueue = new Array<string>();
  private snackBarOpen = false;

  constructor(private snackBar: MatSnackBar) {

  }

  public createError(message: string) {
    this.snackBarOpen = true;
    const currentSnackbar = this.snackBar.open(message, '', {
      duration: 3000,
      panelClass: 'snackbar-error'
    });
    currentSnackbar.afterDismissed().subscribe(() => {
      if (this.errorQueue.length > 0) {
        this.createError(this.errorQueue.shift());
      } else {
        this.snackBarOpen = false;
      }
    });
  }
}
