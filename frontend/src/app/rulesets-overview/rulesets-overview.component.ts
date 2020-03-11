import { Component, OnInit } from '@angular/core';
import { RulesetDto, RulesetsService } from '@ovide/backend';

@Component({
  selector: 'ovide-rulesets-overview',
  templateUrl: './rulesets-overview.component.html',
  styleUrls: ['./rulesets-overview.component.scss']
})
export class RulesetsOverviewComponent implements OnInit {

  rulesets: RulesetDto[];

  constructor(
    private rulesetService: RulesetsService
  ) { }

  ngOnInit(): void {
    
    this.rulesetService.getAllRulesets().subscribe(
      rulesets => this.rulesets = rulesets
    );
  }

}
