import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs/operators';
import { RulesetDto, RulesetsService } from '@ovide/backend';
import { Observable } from 'rxjs';

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss']
})
export class RulesetEditorComponent implements OnInit {

  ruleset$: Observable<RulesetDto>;

  constructor(
    private route: ActivatedRoute,
    private rulesetsService: RulesetsService
  ) { }

  ngOnInit(): void {
    this.ruleset$ = this.route.paramMap
    .pipe(
      map(params => params.get('id')),
      switchMap(id => this.rulesetsService.getRuleset(id)),
    );
  }

}
