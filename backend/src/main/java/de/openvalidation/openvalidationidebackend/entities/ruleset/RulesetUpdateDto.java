package de.openvalidation.openvalidationidebackend.entities.ruleset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesetUpdateDto {
  private String name;
  private String description;
  private String rules;
}
