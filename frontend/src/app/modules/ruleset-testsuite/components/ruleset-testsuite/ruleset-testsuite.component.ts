import { animate, query, stagger, style, transition, trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ErrorHandlerService, SchemaService } from '@ovide/core';
import { RulesetDto, RulesetsBackendService } from '@ovide/core/backend';
import { OVLanguageServerService } from '@ovide/modules/ruleset-editor';
import * as faker from 'faker';
import { Subscription } from 'rxjs';
import { map, switchMap, tap } from 'rxjs/operators';


@Component({
  selector: 'ovide-ruleset-testsuite',
  templateUrl: './ruleset-testsuite.component.html',
  styleUrls: ['./ruleset-testsuite.component.scss'],
  animations: [
    trigger('editorAnimation', [
      transition(':enter', [
        style({ transform: 'scale(0.9)', opacity: 0.2 }),
        animate('.4s ease-in-out')
      ])
    ]),
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
  ],
  providers: [OVLanguageServerService]
})
export class RulesetTestsuiteComponent implements OnInit {

  tableDisplayedColumns: string [];
  tableDataSource;
  private attributeColumns: string[];
  private subscriptions = new Subscription();
  private ruleset: RulesetDto;
  attributes;

  editorText: FormControl;

  constructor(
    private route: ActivatedRoute,
    private rulesetsBackendService: RulesetsBackendService,
    private schemaService: SchemaService,
    private errorHandlerService: ErrorHandlerService,
    private ovLanguageServerService: OVLanguageServerService
  ) { }

  ngOnInit(): void {
    this.editorText = new FormControl({value: '', disabled: true});

    this.subscriptions.add(this.route.paramMap.pipe(
      map(params => params.get('id')),
      switchMap(id => this.rulesetsBackendService.getRuleset(id))
    ).subscribe(
      ruleset => {
        this.ruleset = ruleset;
        this.editorText.setValue(ruleset.rules);
        this.schemaService.setSchema(ruleset.schemaId);
      },
      () => this.errorHandlerService.createError('Error fetching ruleset.')
    ));

    this.subscriptions.add(
      this.schemaService.schemaId$.pipe(
        switchMap(schemaId => this.schemaService.getAllAttributesFromSchema(schemaId)),
        tap(attributes => this.fillTableWithSampleData(attributes))
      ).subscribe()
    );

    this.subscriptions.add(
      this.schemaService.schemaId$.subscribe(
        schemaId => this.ovLanguageServerService.setSchema(schemaId)
      )
    );

  }

  private fillTableWithSampleData(attributes) {
    this.attributes = attributes;
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
  }

  private updateAttributeColumns(attributes) {
    this.attributeColumns = [];
    for (const attribute of attributes) {
      this.attributeColumns.push(attribute.name);
    }
  }

}
