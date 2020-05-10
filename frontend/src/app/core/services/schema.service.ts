import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AttributesBackendService, SchemaBackendService, AttributeCreateDto, AttributeDto, AttributeUpdateDto } from '../backend';

@Injectable({
  providedIn: 'root'
})
export class SchemaService {
  private schemaId = new Subject<string>();
  public schemaId$ = this.schemaId.asObservable();

  constructor(
    private attributesBackendService: AttributesBackendService,
    private schemaBackendService: SchemaBackendService
  ) { }

  setSchema(schemaId) {
    this.schemaId.next(schemaId);
  }

  getAllAttributesFromSchema(schemaId: string) {
    return this.attributesBackendService.getAllAttributesFromSchema(schemaId);
  }

  addAttributesToSchema(schemaId: string, attributeCreateDtos: AttributeCreateDto[]): Observable<Array<AttributeDto>> {
    return this.attributesBackendService.addAttributesToSchema(schemaId, attributeCreateDtos).pipe(
      tap(() => this.schemaId.next(schemaId))
    );
  }

  deleteAttributeFromSchema(schemaId: string, attributeId: string) {
    this.schemaId.next(schemaId);
    return this.attributesBackendService.deleteAttributeFromSchema(schemaId, attributeId).pipe(
      tap(() => this.schemaId.next(schemaId))
    );
  }

  updateAttributeFromSchema(schemaId: string, attributeId: string, updateDto: AttributeUpdateDto) {
    this.schemaId.next(schemaId);
    return this.attributesBackendService.updateAttributeFromSchema(schemaId, attributeId, updateDto).pipe(
      tap(() => this.schemaId.next(schemaId))
    );
  }

  exportSchema(schemaId) {
    return this.schemaBackendService.exportSchema('application/json', schemaId);
  }
}
