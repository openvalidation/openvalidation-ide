import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';

@Component({
  selector: 'ovide-app',
  templateUrl: './ovide-app.component.html',
  styleUrls: ['./ovide-app.component.scss']
})
export class OvideAppComponent implements OnInit {
  isEditing: boolean;
  router: Router;
  route: ActivatedRoute;
  currentRuleset: number;

  constructor(router: Router, route: ActivatedRoute) {
    this.router = router;
    this.route = route;
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
        console.log(this.currentRuleset);
      } else {
        this.isEditing = false;
        this.currentRuleset = null;
      }
    }
  }
}
