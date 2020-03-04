package de.openvalidation.openvalidationidebackend.ruleset.schema;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SchemaDto {
  private Set<AttributeDto> attributes;
}
