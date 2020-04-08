import { Component, OnInit } from '@angular/core';
import * as faker from 'faker';
import { Subscription } from 'rxjs';
import { switchMap, map, tap } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { RulesetsBackendService, RulesetDto } from '@ovide/backend';
import { SchemaService } from '@ovide/services/schema.service';
import { ErrorHandlerService } from '@ovide/services/error-handler.service';
import { trigger, transition, query, style, stagger, animate } from '@angular/animations';

@Component({
  selector: 'ovide-ruleset-testsuite',
  templateUrl: './ruleset-testsuite.component.html',
  styleUrls: ['./ruleset-testsuite.component.scss'],
  animations: [
    trigger('scaleAnimation', [
      transition(':enter', [
        query('*', [
          style({ transform: 'scale(0.5)', opacity: 0 }),
          stagger(30, [
            animate('.5s ease-out')
          ]),
        ], { optional: true })
      ])
    ])
  ]
})
export class RulesetTestsuiteComponent implements OnInit {

  tableDisplayedColumns: string [];
  tableDataSource;
  private attributeColumns: string[];
  private subscriptions = new Subscription();
  private ruleset: RulesetDto;
  private attributes;

  constructor(
    private route: ActivatedRoute,
    private rulesetsBackendService: RulesetsBackendService,
    private schemaService: SchemaService,
    private errorHandlerService: ErrorHandlerService
  ) {
  }

  ngOnInit(): void {

    this.subscriptions.add(this.route.paramMap.pipe(
      map(params => params.get('id')),
      switchMap(id => this.rulesetsBackendService.getRuleset(id))
    ).subscribe(
      ruleset => {
        this.ruleset = ruleset;
        this.schemaService.setSchema(ruleset.schemaId);
      },
      () => this.errorHandlerService.createError('Error fetching ruleset.')
    ));

    this.subscriptions.add(
      this.schemaService.schemaId$.pipe(
        switchMap(schemaId => this.schemaService.getAllAttributesFromSchema(schemaId)),
        tap(attributes => {
          this.attributes = attributes;
          console.log(attributes);
          this.updateAttributeColumns(attributes);
          this.tableDisplayedColumns = this.attributeColumns.concat(['expected', 'passed']);

          const fakeData = () => {
            const object = {
              expected: faker.random.boolean(),
              passed: faker.random.boolean()
            };
            for (const attribute of this.attributes) {
              switch (attribute.attributeType) {
                case 'BOOLEAN':
                  object[attribute.name] = faker.random.boolean();
                  break;
                case 'NUMBER':
                  object[attribute.name] = faker.random.number({ min: 10, max: 80 });
                  break;
                default:
                  object[attribute.name] = faker.lorem.word();
                  break;
              }
            }
            return object;
          };
          this.tableDataSource = Array.from({ length: 5 }, fakeData);
        })
      ).subscribe()
    );
  }

  private updateAttributeColumns(attributes) {
    this.attributeColumns = [];
    for (const attribute of attributes) {
      this.attributeColumns.push(attribute.name);
    }
  }
}
