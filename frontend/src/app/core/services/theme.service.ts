import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { ReplaySubject } from 'rxjs';

export enum AvailableThemes {
  dark,
  light
}

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  private renderer: Renderer2;
  private activeTheme: AvailableThemes;
  private darkThemeActive = new ReplaySubject<boolean>(1);
  public darkThemeActive$ = this.darkThemeActive.asObservable();

  constructor(private rendererFactory: RendererFactory2) {
    this.renderer = this.rendererFactory.createRenderer(null, null);
    this.activeTheme = AvailableThemes.dark;
    this.darkThemeActive.next(true);
  }

  public enableTheme(theme: AvailableThemes): void {
    if (theme === this.activeTheme) { return; }
    switch (theme) {
      case AvailableThemes.light:
        this.renderer.removeClass(document.documentElement, 'dark-theme');
        this.renderer.addClass(document.documentElement, 'light-theme');
        this.activeTheme = AvailableThemes.light;
        this.darkThemeActive.next(false);
        break;
      case AvailableThemes.dark:
        this.renderer.removeClass(document.documentElement, 'light-theme');
        this.renderer.addClass(document.documentElement, 'dark-theme');
        this.activeTheme = AvailableThemes.dark;
        this.darkThemeActive.next(true);
        break;
    }
  }
}
