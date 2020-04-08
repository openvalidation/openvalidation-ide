package de.openvalidation.openvalidationidebackend.domain.schema;

import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SchemaDto {
  private UUID schemaId;
  private Set<AttributeDto> attributes;
}
