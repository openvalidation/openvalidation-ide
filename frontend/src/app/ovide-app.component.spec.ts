import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OvideAppComponent } from './ovide-app.component';

describe('OvideAppComponent', () => {
  let component: OvideAppComponent;
  let fixture: ComponentFixture<OvideAppComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OvideAppComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OvideAppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
