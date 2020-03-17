import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DesignTestComponent } from './design-test.component';

describe('DesignTestComponent', () => {
  let component: DesignTestComponent;
  let fixture: ComponentFixture<DesignTestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DesignTestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DesignTestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
