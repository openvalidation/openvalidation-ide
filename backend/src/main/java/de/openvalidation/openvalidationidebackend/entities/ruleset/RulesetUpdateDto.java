package de.openvalidation.openvalidationidebackend.entities.ruleset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesetUpdateDto {
  @Size(max = 255)
  private String name;
  @Size(max = 255)
  private String description;
  private String rules;
}
