package de.openvalidation.openvalidationidebackend.entities.ruleset;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RulesetUpdateDto {
  private String name;
  private String description;
  private String rules;
}
