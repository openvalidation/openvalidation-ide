import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map, switchMap } from 'rxjs/operators';
import { RulesetsService, RulesetDto } from '@ovide/backend';
import { Observable } from 'rxjs';
import { ThemeService } from '@ovide/services/theme.service';

@Component({
  selector: 'ovide-ruleset-editor',
  templateUrl: './ruleset-editor.component.html',
  styleUrls: ['./ruleset-editor.component.scss']
})
export class RulesetEditorComponent implements OnInit {
  editorOptions = {
    theme: 'vs-dark',
    language: 'ov',
    minimap: {
      enabled: false
    },
    lineNumbers: false
  };
  code = 'Der Text muss Validaria sein';
  ruleset$: Observable<RulesetDto>;
  private editor;

  constructor(
    private route: ActivatedRoute,
    private rulesetsService: RulesetsService,
    private themeService: ThemeService,
  ) { }

  ngOnInit(): void {
    this.ruleset$ = this.route.paramMap
    .pipe(
      map(params => params.get('id')),
      switchMap(id => this.rulesetsService.getRuleset(id)),
    );

    this.themeService.darkThemeActive$.subscribe((isDark) => {
      if (this.editor !== undefined) {
        this.editor.setTheme(isDark ? 'vs-dark' : 'vs');
      }
    });
  }

  monacoInit(editor) {
    this.editor = monaco.editor;
    console.log('monaco initialized');
  }
}
