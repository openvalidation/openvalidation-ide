import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import * as faker from 'faker';
import { Subscription } from 'rxjs';
import { switchMap, map, tap, take } from 'rxjs/operators';
import { ActivatedRoute } from '@angular/router';
import { RulesetsBackendService, RulesetDto } from '@ovide/backend';
import { SchemaService } from '@ovide/services/schema.service';
import { ErrorHandlerService } from '@ovide/services/error-handler.service';
import { trigger, transition, query, style, stagger, animate } from '@angular/animations';
import { LanguageEnum } from 'ov-language-server-types';
import { OVLanguageServerService } from '@ovide/services/ov-language-server.service';
import { ThemeService } from '@ovide/services/theme.service';
import { FormControl } from '@angular/forms';

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

  private editor;
  editorText: FormControl;
  editorInitDone = false;

  editorOptions = {
    language: 'ov',
    fontFamily: 'Source Code Pro',
    minimap: {
      enabled: false
    },
    lineNumbers: false,
    folding: false,
    readOnly: true,
  };

  constructor(
    private route: ActivatedRoute,
    private rulesetsBackendService: RulesetsBackendService,
    private schemaService: SchemaService,
    private errorHandlerService: ErrorHandlerService,
    private ovLanguageServerService: OVLanguageServerService,
    private changeDetectorRef: ChangeDetectorRef,
    private themeService: ThemeService,
  ) { }


  private static readStyleProperty(name: string): string {
    const bodyStyles = window.getComputedStyle(document.documentElement);
    return bodyStyles.getPropertyValue('--' + name).trim();
  }

  ngOnInit(): void {
    this.editorText = new FormControl();
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

    this.subscriptions.add(
      this.themeService.darkThemeActive$.subscribe((isDark) => {
        this.updateTheme(isDark);
      })
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

  private updateTheme(isDark: boolean) {
    if (this.editor !== undefined) {
      monaco.editor.defineTheme('ovide-theme', {
        base: isDark ? 'vs-dark' : 'vs',
        inherit: true,
        rules: [
          { token: 'keyword.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-keyword') },
          { token: 'string.unquoted.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-type-string') },
          { token: 'string.error.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-error') },
          { token: 'variable.parameter.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-variable') },
          { token: 'variable.number.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-type-number') },
          { token: 'variable.text.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-type-string') },
          { token: 'variable.boolean.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-type-boolean') },
          { token: 'constant.numeric.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-type-number') },
          { token: 'constant.boolean.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-type-boolean') },
          { token: 'comment.block.ov', foreground: RulesetTestsuiteComponent.readStyleProperty('editor-comment') }
        ],
        colors: {
          'editor.background': RulesetTestsuiteComponent.readStyleProperty('editor-background'),
          'editor.lineHighlightBorder': RulesetTestsuiteComponent.readStyleProperty('editor-lineHighlightBorder'),
          'editor.selectionBackground': RulesetTestsuiteComponent.readStyleProperty('editor-selectionBackground'),
        }
      });
      monaco.editor.setTheme('ovide-theme');
    }
  }

  monacoInit(editor) {
    this.editor = editor;
    this.editorInitDone = true;
    this.changeDetectorRef.detectChanges();

    this.themeService.darkThemeActive$.pipe(take(1)).subscribe((isDark) => {
        this.updateTheme(isDark);
      }
    );

    monaco.editor.setTheme('ovide-theme');
    this.ovLanguageServerService.initialize(editor);
    this.ovLanguageServerService.setCulture('en');
    this.ovLanguageServerService.setOutputLanguage(LanguageEnum.JavaScript);
  }
}
