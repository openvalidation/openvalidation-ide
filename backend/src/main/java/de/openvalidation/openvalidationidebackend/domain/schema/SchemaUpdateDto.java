package de.openvalidation.openvalidationidebackend.domain.schema;

import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SchemaUpdateDto {
  private Set<AttributeUpdateDto> attributes;
}
