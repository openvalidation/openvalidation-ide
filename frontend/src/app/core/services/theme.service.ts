import { Injectable, Renderer2, RendererFactory2 } from '@angular/core';
import { ReplaySubject } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

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

  constructor(private rendererFactory: RendererFactory2,
              private cookieService: CookieService) {
    this.renderer = this.rendererFactory.createRenderer(null, null);
    this.activeTheme = AvailableThemes.dark;
    if (cookieService.check('darkThemeActive') && cookieService.get('darkThemeActive') === '0') {
      this.darkThemeActive.next(false);
      this.enableTheme(AvailableThemes.light);
    } else {
      this.darkThemeActive.next(true);
      this.enableTheme(AvailableThemes.dark);
    }
  }

  public enableTheme(theme: AvailableThemes): void {
    if (theme === this.activeTheme) { return; }
    switch (theme) {
      case AvailableThemes.light:
        this.renderer.removeClass(document.documentElement, 'dark-theme');
        this.renderer.addClass(document.documentElement, 'light-theme');
        this.cookieService.set('darkThemeActive', '0', 365, '/');
        this.activeTheme = AvailableThemes.light;
        this.darkThemeActive.next(false);
        break;
      case AvailableThemes.dark:
        this.renderer.removeClass(document.documentElement, 'light-theme');
        this.renderer.addClass(document.documentElement, 'dark-theme');
        this.cookieService.set('darkThemeActive', '1', 365, '/');
        this.activeTheme = AvailableThemes.dark;
        this.darkThemeActive.next(true);
        break;
    }
  }
}
