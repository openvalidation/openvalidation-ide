package de.openvalidation.openvalidationidebackend.ruleset.schema;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SchemaUpdateDto {
  private Set<AttributeUpdateDto> attributes;
}
