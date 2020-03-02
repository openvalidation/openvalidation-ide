import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RulesetCreatorComponent } from './ruleset-creator.component';

describe('RulesetCreatorComponent', () => {
  let component: RulesetCreatorComponent;
  let fixture: ComponentFixture<RulesetCreatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RulesetCreatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RulesetCreatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
