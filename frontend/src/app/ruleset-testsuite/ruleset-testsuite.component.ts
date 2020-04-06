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

  constructor() { }

  ngOnInit(): void {
    this.tableDisplayedColumns = ['name', 'age', 'street', 'city', 'expected', 'passed'];

    const fakeData = (): TableDataRow => {
      return {
        name: faker.name.findName(),
        age: faker.random.number({ min: 10, max: 80 }),
        street: faker.address.streetName(),
        city: faker.address.city(),
        expected: faker.random.boolean(),
        passed: faker.random.boolean(),
      };
    };
    this.tableDataSource = Array.from({ length: 5 }, fakeData);
  }

}
