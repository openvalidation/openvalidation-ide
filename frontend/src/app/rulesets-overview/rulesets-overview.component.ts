import { Component, OnInit } from '@angular/core';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';
import { Observable } from 'rxjs';
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'ovide-rulesets-overview',
  templateUrl: './rulesets-overview.component.html',
  styleUrls: ['./rulesets-overview.component.scss'],
  animations: [
    trigger('cardInAnimation', [
      transition(':enter', [
        style({ transform: 'scale(0.5)' }),
        animate('.4s ease-out', style({}))
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
