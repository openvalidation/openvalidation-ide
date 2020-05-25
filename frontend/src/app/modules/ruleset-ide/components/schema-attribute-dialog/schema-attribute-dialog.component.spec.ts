import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SchemaAttributeDialogComponent } from './schema-attribute-dialog.component';

describe('SchemaAttributeDialogComponent', () => {
  let component: SchemaAttributeDialogComponent;
  let fixture: ComponentFixture<SchemaAttributeDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SchemaAttributeDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchemaAttributeDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
