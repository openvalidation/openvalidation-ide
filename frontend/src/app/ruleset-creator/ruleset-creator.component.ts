import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { RulesetsService, RulesetCreateDto } from '@ovide/backend';

import * as faker from 'faker';
import { Router } from '@angular/router';


@Component({
  selector: 'ovide-ruleset-creator',
  templateUrl: './ruleset-creator.component.html',
  styleUrls: ['./ruleset-creator.component.scss']
})
export class RulesetCreatorComponent implements OnInit {

  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private rulesetsService: RulesetsService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: [''],
      creator: ['', [Validators.required]]
    });
  }

  submit() {
    this.form.disable();

    const newRuleset: RulesetCreateDto = {
      name: this.form.value.name,
      description: this.form.value.description,
      createdBy: this.form.value.creator
    }

    this.rulesetsService.createRuleset(newRuleset).subscribe(
      success => { 
        this.router.navigate(['/rulesets' ,success.rulesetId, 'edit']);
      },
      error => {
        console.error(error);
      }
    );
  }

  createRandom() {
    this.rulesetsService.createRuleset({
      name: faker.company.catchPhrase(),
      createdBy: faker.name.findName(),
      description: faker.lorem.paragraph()
    }).subscribe();
  }

}
