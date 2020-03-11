package de.openvalidation.openvalidationidebackend.entities.schema;

import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SchemaUpdateDto {
  private Set<AttributeUpdateDto> attributes;
}
