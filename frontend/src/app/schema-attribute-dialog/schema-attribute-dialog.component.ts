import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SchemaAttribute } from '@ovide/schema-editor';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'ovide-schema-attribute-dialog',
  templateUrl: './schema-attribute-dialog.component.html',
  styleUrls: ['./schema-attribute-dialog.component.scss']
})
export class SchemaAttributeDialogComponent implements OnInit {

  title: string;
  attribute: SchemaAttribute;

  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<SchemaAttributeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SchemaAttributeDialogData
  ) {
    if (data.type === SchemaAttributeDialogType.edit && (this.data.attribute === null || this.data.attribute === undefined)) {
      throw TypeError("When SchemaAttributeDialogType is === edit, SchemaAttributeDialogData.attribut cannot be undefined or null");
    }
  }

  ngOnInit(): void {
    switch(this.data.type) {
      case SchemaAttributeDialogType.create:
        this.title = 'Add new attribute';
        this.attribute = { name: '', type: 'string' };
        break;
      case SchemaAttributeDialogType.edit:
        this.title = 'Edit attribute';
        this.attribute = this.data.attribute;
        break;
    }

    this.form = this.formBuilder.group({
      name: [this.attribute.name, [Validators.required]],
      type: [this.attribute.type, [Validators.required]]
    });
  }

  submit() {
    // TODO: map to model
    this.dialogRef.close(this.form.value);
  }

}

export interface SchemaAttributeDialogData {
  type: SchemaAttributeDialogType,
  attribute?: SchemaAttribute
}

export enum SchemaAttributeDialogType {
  create,
  edit
}
