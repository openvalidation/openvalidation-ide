import { Component, OnInit } from '@angular/core';
import * as faker from 'faker';
import { TableDataRow } from '@ovide/ruleset-testsuite';

@Component({
  selector: 'ovide-design-test',
  templateUrl: './design-test.component.html',
  styleUrls: ['./design-test.component.scss']
})
export class DesignTestComponent implements OnInit {

  constructor() { }

  tableDisplayedColumns: string [];
  tableDataSource: TableDataRow [];

  ngOnInit(): void {
    this.tableDisplayedColumns = ['name', 'age', 'street', 'city', 'expected', 'passed'];

    const fakeData = (): TableDataRow => {
      return {
        name: faker.name.findName(),
        age: faker.random.number({min: 10, max: 80}),
        street: faker.address.streetName(),
        city: faker.address.city(),
        expected: faker.random.boolean(),
        passed: faker.random.boolean(),
      };
    };
    this.tableDataSource = Array.from({length: 8}, fakeData);
  }

}
