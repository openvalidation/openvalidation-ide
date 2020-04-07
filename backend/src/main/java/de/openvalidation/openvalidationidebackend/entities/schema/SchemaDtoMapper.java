package de.openvalidation.openvalidationidebackend.entities.schema;

import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeDtoMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = AttributeDtoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SchemaDtoMapper {
  Schema toSchemaEntity(SchemaUpdateDto schemaUpdateDto);
  SchemaDto toSchemaDto(Schema schema);
  List<SchemaDto> toSchemaDtoList(List<Schema> schemas);
}
