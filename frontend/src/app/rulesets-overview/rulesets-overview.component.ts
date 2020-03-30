import { Component, OnInit } from '@angular/core';
import { RulesetDto, RulesetsBackendService } from '@ovide/backend';

@Component({
  selector: 'ovide-rulesets-overview',
  templateUrl: './rulesets-overview.component.html',
  styleUrls: ['./rulesets-overview.component.scss']
})
export class RulesetsOverviewComponent implements OnInit {

  rulesets: RulesetDto[];

  constructor(
    private rulesetBackendService: RulesetsBackendService
  ) { }

  ngOnInit(): void {

    this.rulesetBackendService.getAllRulesets().subscribe(
      rulesets => this.rulesets = rulesets
    );
  }

}
