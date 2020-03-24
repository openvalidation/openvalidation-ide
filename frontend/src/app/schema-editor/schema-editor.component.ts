import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SchemaAttributeDialogComponent, SchemaAttributeDialogMode } from '@ovide/schema-attribute-dialog';
import { AttributeCreateDto, AttributeDto, AttributesBackendService, AttributeUpdateDto } from '@ovide/backend';
import { ThemeService } from '@ovide/services/theme.service';

@Component({
  selector: 'ovide-schema-editor',
  templateUrl: './schema-editor.component.html',
  styleUrls: ['./schema-editor.component.scss']
})
export class SchemaEditorComponent implements OnInit {

  private _schemaId: string;
  @Input() set schemaId(value: string) {
    this._schemaId = value;
    if (value !== null && value !== undefined) {
      this.initialize();
    }
  }

  public attributes: AttributeDto[];

  constructor(
    public dialog: MatDialog,
    private attributeService: AttributesBackendService,
    public themeService: ThemeService
  ) { }

  ngOnInit(): void {}

  private initialize() {
    this.attributeService.getAllAttributesFromSchema(this._schemaId)
    .subscribe(
      success => this.attributes = success,
      error => console.error(error)
    );
  }

  add(): void {
    const dialogRef = this.dialog.open(SchemaAttributeDialogComponent, {
      data: { mode: SchemaAttributeDialogMode.create }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        const createDto: AttributeCreateDto = {
          attributeType: result.attributeType,
          name: result.name
        };
        this.attributeService.addAttributesToSchema(this._schemaId, [createDto])
        .subscribe(
          success => this.attributes.push(success[0]),
          error => console.error(error)
        );
      }
    });
  }

  remove(attribute: AttributeDto): void {
    const index = this.attributes.indexOf(attribute);
    if (index >= 0) {
      this.attributeService.deleteAttributeFromSchema(this._schemaId, attribute.attributeId)
      .subscribe(
        success => this.attributes.splice(index, 1),
        error => console.error(error)
      );
    }
  }

  edit(attribute: AttributeDto) {
    const dialogRef = this.dialog.open(SchemaAttributeDialogComponent, {
      data: { mode: SchemaAttributeDialogMode.edit, attribute }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result !== undefined) {
        const updateDto: AttributeUpdateDto = {
          attributeType: result.attributeType,
          name: result.name
        };
        this.attributeService.updateAttributeFromSchema(this._schemaId, attribute.attributeId, updateDto)
        .subscribe(
          success => {
            const index = this.attributes.findIndex(element => element.attributeId === attribute.attributeId);
            if (index >= 0) {
              this.attributes[index] = success;
            }
          },
          error => console.error(error)
        );
      }
    });
  }

}
