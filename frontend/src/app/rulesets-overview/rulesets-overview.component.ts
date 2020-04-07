import { Component, OnInit } from '@angular/core';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';
import { Observable, of } from 'rxjs';
import { trigger, transition, style, animate, query, stagger } from '@angular/animations';
import { catchError } from 'rxjs/operators';
import { ErrorHandlerService } from '@ovide/services/error-handler.service';
import { Router } from '@angular/router';

@Component({
  selector: 'ovide-rulesets-overview',
  templateUrl: './rulesets-overview.component.html',
  styleUrls: ['./rulesets-overview.component.scss'],
  animations: [
    trigger('cardInAnimation', [
      transition(':enter', [
        query('.ruleset-card', [
          style({ transform: 'scale(0.5)', opacity: 0 }),
          stagger(15, [
            animate('.4s ease-out')
          ]),
        ])
      ])
    ])
  ]
})
export class RulesetsOverviewComponent implements OnInit {

  rulesets$: Observable<RulesetDto[]>;

  constructor(
    private rulesetsBackendService: RulesetsBackendService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService
  ) { }

  ngOnInit(): void {

    this.rulesets$ = this.rulesetsBackendService.getAllRulesets().pipe(
      catchError(err => {
        this.errorHandlerService.createError('Error fetching rulesets.');
        return of([]);
      })
    );

  }

  add() {
    this.rulesetsBackendService.createRuleset().subscribe(
      success => {
        this.router.navigate(['/rulesets' , success.rulesetId, 'edit']);
      },
      () => this.errorHandlerService.createError('Error creating ruleset.')
    );
  }

}
