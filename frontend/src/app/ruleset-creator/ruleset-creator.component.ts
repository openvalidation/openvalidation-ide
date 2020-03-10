import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'ovide-ruleset-creator',
  templateUrl: './ruleset-creator.component.html',
  styleUrls: ['./ruleset-creator.component.scss']
})
export class RulesetCreatorComponent implements OnInit {

  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      name: ['', [Validators.required]],
      description: ['']
    });
  }

  submit() {

  }

}
