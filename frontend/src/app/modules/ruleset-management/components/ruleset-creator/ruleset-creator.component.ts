import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RulesetCreateDto, RulesetsBackendService } from '@ovide/core/backend';

import { Router } from '@angular/router';
import { trigger, transition, style, animate } from '@angular/animations';
import { ErrorHandlerService } from '@ovide/core';


@Component({
  selector: 'ovide-ruleset-creator',
  templateUrl: './ruleset-creator.component.html',
  styleUrls: ['./ruleset-creator.component.scss'],
  animations: [
    trigger('fadeIn', [
      transition(':enter', [
        style({ transform: 'scale(0.9)', opacity: 0.2 }),
        animate('.3s ease-out')
      ])
    ])
  ]
})
export class RulesetCreatorComponent implements OnInit {

  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private rulesetsBackendService: RulesetsBackendService,
    private router: Router,
    private errorHandlerService: ErrorHandlerService
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
    };

    this.rulesetsBackendService.createRuleset(newRuleset).subscribe(
      success => {
        this.router.navigate(['/rulesets' , success.rulesetId, 'edit']);
      },
      () => this.errorHandlerService.createError('Error creating ruleset.')
    );
  }
}
