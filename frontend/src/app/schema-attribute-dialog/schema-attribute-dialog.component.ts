import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { AttributeDto } from '@ovide/backend';

@Component({
  selector: 'ovide-schema-attribute-dialog',
  templateUrl: './schema-attribute-dialog.component.html',
  styleUrls: ['./schema-attribute-dialog.component.scss']
})
export class SchemaAttributeDialogComponent implements OnInit {

  title: string;
  attribute: AttributeDto;

  form: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<SchemaAttributeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: SchemaAttributeDialogData
  ) {
    if (data.mode === SchemaAttributeDialogMode.edit && (this.data.attribute === null || this.data.attribute === undefined)) {
      throw TypeError("When SchemaAttributeDialogMode is === edit, SchemaAttributeDialogData.attribut cannot be undefined or null");
    }
  }

  ngOnInit(): void {
    switch(this.data.mode) {
      case SchemaAttributeDialogMode.create:
        this.title = 'Add new attribute';
        this.attribute = { name: '', attributeType: 'TEXT' };
        break;
      case SchemaAttributeDialogMode.edit:
        this.title = 'Edit attribute';
        this.attribute = this.data.attribute;
        break;
    }

    this.form = this.formBuilder.group({
      name: [this.attribute.name, [Validators.required]],
      attributeType: [this.attribute.attributeType, [Validators.required]]
    });
  }

  submit() {
    this.dialogRef.close(this.form.value);
  }

}

export interface SchemaAttributeDialogData {
  mode: SchemaAttributeDialogMode,
  attribute?: AttributeDto
}

export enum SchemaAttributeDialogMode {
  create,
  edit
}
