import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OvideLogoComponent } from './ovide-logo.component';

describe('OvideLogoComponent', () => {
  let component: OvideLogoComponent;
  let fixture: ComponentFixture<OvideLogoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OvideLogoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OvideLogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
