import { Component, OnInit } from '@angular/core';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';
import { Observable } from 'rxjs';
import { trigger, transition, style, animate, query, stagger } from '@angular/animations';

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
    private rulesetBackendService: RulesetsBackendService
  ) { }

  ngOnInit(): void {

    this.rulesets$ = this.rulesetBackendService.getAllRulesets();

  }

}
