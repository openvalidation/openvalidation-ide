import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { SchemaAttributeDialogComponent, SchemaAttributeDialogMode } from '@ovide/schema-attribute-dialog';
import { AttributeCreateDto, AttributeDto, AttributeUpdateDto } from '@ovide/backend';
import { ThemeService } from '@ovide/services/theme.service';
import { SchemaService } from '@ovide/services/schema.service';
import { trigger, transition, style, animate, stagger, query } from '@angular/animations';
import { ErrorHandlerService } from '@ovide/services/error-handler.service';

@Component({
  selector: 'ovide-schema-editor',
  templateUrl: './schema-editor.component.html',
  styleUrls: ['./schema-editor.component.scss'],
  animations: [
    trigger('attributeAnimation', [
      transition(':enter', [
        query('*', [
          style({ transform: 'scale(0.5)', opacity: 0 }),
          stagger(30, [
            animate('.2s ease-out')
          ]),
        ])
      ])
    ])
  ]
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
    private schemaService: SchemaService,
    public themeService: ThemeService,
    private errorHandlerService: ErrorHandlerService
  ) {
  }

  ngOnInit(): void {
  }

  private initialize() {
    this.attributes = undefined;
    this.schemaService.getAllAttributesFromSchema(this._schemaId)
      .subscribe(
        success => this.attributes = success,
        () => this.errorHandlerService.createError('Error fetching attributes.')
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
        this.schemaService.addAttributesToSchema(this._schemaId, [createDto])
          .subscribe(
            success => this.attributes.push(success[0]),
            () => this.errorHandlerService.createError('Error adding attribute to schema.')
          );
      }
    });
  }

  remove(attribute: AttributeDto): void {
    const index = this.attributes.indexOf(attribute);
    if (index >= 0) {
      this.schemaService.deleteAttributeFromSchema(this._schemaId, attribute.attributeId)
        .subscribe(
          () => this.attributes.splice(index, 1),
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
        this.schemaService.updateAttributeFromSchema(this._schemaId, attribute.attributeId, updateDto)
          .subscribe(
            success => {
              const index = this.attributes.findIndex(element => element.attributeId === attribute.attributeId);
              if (index >= 0) {
                this.attributes[index] = success;
              }
            },
            () => this.errorHandlerService.createError('Error updating attribute.')
          );
      }
    });
  }

}
