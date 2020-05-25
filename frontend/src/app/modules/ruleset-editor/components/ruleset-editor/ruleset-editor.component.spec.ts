import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RulesetEditorComponent } from './ruleset-editor.component';

describe('RulesetEditorComponent', () => {
  let component: RulesetEditorComponent;
  let fixture: ComponentFixture<RulesetEditorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RulesetEditorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RulesetEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
