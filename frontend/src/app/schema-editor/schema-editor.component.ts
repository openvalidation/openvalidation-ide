import { Component, OnInit, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SchemaAttributeDialogComponent, SchemaAttributeDialogMode } from '@ovide/schema-attribute-dialog';
import { AttributeDto, AttributesService } from '@ovide/backend';

@Component({
  selector: 'ovide-schema-editor',
  templateUrl: './schema-editor.component.html',
  styleUrls: ['./schema-editor.component.scss']
})
export class SchemaEditorComponent implements OnInit {

  private _schemaId: string;
  @Input() set schemaId(value: string) {
    this._schemaId = value;
    this.initialize();
  }

  public attributes: AttributeDto[];

  constructor(
    public dialog: MatDialog,
    private attributesService: AttributesService
  ) { }

  ngOnInit(): void {
    this.attributes = [
      { name: 'name', attributeType: 'TEXT' },
      { name: 'age', attributeType: 'NUMBER' },
      { name: 'city', attributeType: 'BOOLEAN' }
    ];
  }

  private initialize() {
    // TODO get attributes from schema
  }

  add(): void {
    const dialogRef = this.dialog.open(SchemaAttributeDialogComponent, {
      data: { mode: SchemaAttributeDialogMode.create }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.attributes.push(result);
        // TODO add attributes to schema
      }
    });
  }

  remove(attribute: AttributeDto): void {
    const index = this.attributes.indexOf(attribute);
    if (index >= 0) {
      this.attributes.splice(index, 1);
      // TODO remove attributes to schema
    }
  }

  edit(attribute: AttributeDto) {
    const dialogRef = this.dialog.open(SchemaAttributeDialogComponent, {
      data: { mode: SchemaAttributeDialogMode.edit, attribute }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        attribute.name = result.name;
        attribute.attributeType = result.attributeType;
        // TODO update attributes in schema
      }
    });
  }

}
