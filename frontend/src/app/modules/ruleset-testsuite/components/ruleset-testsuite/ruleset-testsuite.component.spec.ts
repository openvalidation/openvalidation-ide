import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RulesetTestsuiteComponent } from './ruleset-testsuite.component';

describe('RulesetTestsuiteComponent', () => {
  let component: RulesetTestsuiteComponent;
  let fixture: ComponentFixture<RulesetTestsuiteComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RulesetTestsuiteComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RulesetTestsuiteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
