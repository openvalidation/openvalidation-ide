import { Component, OnInit } from '@angular/core';
import * as faker from 'faker';

export interface TableDataRow {
  name: string;
  age: number;
  street: string;
  city: string;
  expected: boolean;
  passed: boolean;
}

@Component({
  selector: 'ovide-ruleset-testsuite',
  templateUrl: './ruleset-testsuite.component.html',
  styleUrls: ['./ruleset-testsuite.component.scss']
})
export class RulesetTestsuiteComponent implements OnInit {

  tableDisplayedColumns: string [];
  tableDataSource: TableDataRow [];
  graphColorScheme = {
    domain: ['#8BC34A', '#FFC107']
  };
  graphData = [
    {
      "name": "coverage",
      "value": .6
    },
    {
      "name": "succeeded",
      "value": .4
    }
  ]

  constructor() { }

  ngOnInit(): void {
    this.tableDisplayedColumns = ['name', 'age', 'street', 'city', 'expected', 'passed'];

    const fakeData = function(): TableDataRow {
      return {
        name: faker.name.findName(),
        age: faker.random.number({min:10, max:80}),
        street: faker.address.streetName(),
        city: faker.address.city(),
        expected: faker.random.boolean(),
        passed: faker.random.boolean(),
      }
    };
    this.tableDataSource = Array.from({length: 30}, fakeData)
  }

}
