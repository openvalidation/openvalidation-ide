import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AvailableThemes, ThemeService } from '@ovide/services/theme.service';
import { take } from 'rxjs/operators';

@Component({
  selector: 'ovide-app',
  templateUrl: './ovide-app.component.html',
  styleUrls: ['./ovide-app.component.scss']
})
export class OvideAppComponent implements OnInit {
  isEditing: boolean;
  currentRuleset: number;

  constructor(
    private router: Router,
    private themeService: ThemeService
  ) {
  }

  public ngOnInit() {
    this.router.events.subscribe((event) => {
      this.checkIfEditingRoute(event);
    });
  }

  private checkIfEditingRoute(event) {
    if (event instanceof NavigationEnd) {
      const currentUrlSegments = event.urlAfterRedirects.split('/');
      if (currentUrlSegments.length > 2 && currentUrlSegments[1] === 'rulesets' && !isNaN(Number(currentUrlSegments[2]))) {
        this.isEditing = true;
        this.currentRuleset = Number(currentUrlSegments[2]);
      } else {
        this.isEditing = false;
        this.currentRuleset = null;
      }
    }
  }

  public toggleTheme() {
    this.themeService.darkThemeActive$.pipe(take(1)).subscribe(isDark => {
      if (isDark) {
        this.themeService.enableTheme(AvailableThemes.light);
      } else {
        this.themeService.enableTheme(AvailableThemes.dark);
      }
    });
  }
}
