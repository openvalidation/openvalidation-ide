package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaUpdateDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RulesetUpdateDto {
  private String name;
  private String description;
  private String rules;
  private SchemaUpdateDto schema;
}
