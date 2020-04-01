import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AvailableThemes, ThemeService } from '@ovide/services/theme.service';
import { take } from 'rxjs/operators';
import { trigger, transition, query, style, stagger, animate } from '@angular/animations';

@Component({
  selector: 'ovide-app',
  templateUrl: './ovide-app.component.html',
  styleUrls: ['./ovide-app.component.scss'],
  animations: [
    trigger('navigationIconAnimation', [
      transition(':enter', [
        style({ transform: 'scale(0.5)', opacity: 0 }),
        animate('.3s ease-out')
      ]),
      transition(':leave', [
        animate('.3s ease-in', style({ transform: 'scale(0.5)', opacity: 0 }))
      ])
    ])
  ]
})
export class OvideAppComponent implements OnInit {
  isEditing: boolean;
  currentRuleset: string;

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
      if (currentUrlSegments.length > 2 && currentUrlSegments[1] === 'rulesets'
        && currentUrlSegments[2].match(/[0-9A-F]{8}\-[0-9A-F]{4}\-[0-9A-F]{4}\-[0-9A-F]{4}\-[0-9A-F]{12}/i) !== null) {
        this.isEditing = true;
        this.currentRuleset = currentUrlSegments[2];
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
