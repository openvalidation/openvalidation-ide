package de.openvalidation.openvalidationidebackend.entities.schema;

import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.entities.ruleset.RulesetDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class SchemaDto {
  private UUID schemaId;
  private Set<RulesetDto> rulesets;
  private Set<AttributeDto> attributes;
}
