import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SchemaAttributeDialogComponent, SchemaAttributeDialogType } from '@ovide/schema-attribute-dialog';

// ToDo replace with model
export interface SchemaAttribute {
  name: string;
  type: string;
}

@Component({
  selector: 'ovide-schema-editor',
  templateUrl: './schema-editor.component.html',
  styleUrls: ['./schema-editor.component.scss']
})
export class SchemaEditorComponent implements OnInit {

  public attributes: SchemaAttribute[];

  constructor(
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.attributes = [
      { name: 'name', type: 'string' },
      { name: 'age', type: 'number' },
      { name: 'city', type: 'boolean' }
    ];
  }

  add(): void {
    const dialogRef = this.dialog.open(SchemaAttributeDialogComponent, {
      data: { type: SchemaAttributeDialogType.create }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.group('attribute-dialog');
      console.table(result);
      console.groupEnd();
      if (result !== undefined) {
        this.attributes.push(result);
      }
    });
  }

  remove(attribute: SchemaAttribute): void {
    const index = this.attributes.indexOf(attribute);
    if (index >= 0) {
      this.attributes.splice(index, 1);
    }
  }

  edit(attribute: SchemaAttribute) {
    const dialogRef = this.dialog.open(SchemaAttributeDialogComponent, {
      data: { type: SchemaAttributeDialogType.edit, attribute }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.group('attribute-dialog');
      console.table(result);
      console.groupEnd();
      if (result !== undefined) {
        attribute.name = result.name;
        attribute.type = result.type;
      }
    });
  }

}
