package de.openvalidation.openvalidationidebackend.ruleset.schema;

import de.openvalidation.openvalidationidebackend.ruleset.RulesetDto;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeDto;
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
