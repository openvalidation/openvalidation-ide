import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RulesetsOverviewComponent } from './rulesets-overview.component';

describe('RulesetsOverviewComponent', () => {
  let component: RulesetsOverviewComponent;
  let fixture: ComponentFixture<RulesetsOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RulesetsOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RulesetsOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
