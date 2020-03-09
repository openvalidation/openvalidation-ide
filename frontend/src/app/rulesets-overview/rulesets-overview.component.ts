import { Component, OnInit } from '@angular/core';
import * as faker from 'faker';

export interface RulesetDTO {
  id: string,
  name: string;
  description: string;
  createdAt: Date;
  createdBy: string;
}

@Component({
  selector: 'ovide-rulesets-overview',
  templateUrl: './rulesets-overview.component.html',
  styleUrls: ['./rulesets-overview.component.scss']
})
export class RulesetsOverviewComponent implements OnInit {

  rulesets: RulesetDTO[];

  constructor() { }

  ngOnInit(): void {

    const fakeRuleset= function(): RulesetDTO {
      return {
        id: faker.random.uuid(),
        name: faker.company.catchPhrase(),
        description: faker.lorem.paragraph(),
        createdAt: faker.date.past(),
        createdBy: faker.name.findName()
      }
    };

    this.rulesets = Array.from({length: 20}, fakeRuleset)
  }

}
