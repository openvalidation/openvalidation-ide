package de.openvalidation.openvalidationidebackend.ruleset;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RulesetCreateDto {
  @NotNull
  private String name;
  private String description;
  @NotNull
  private String createdBy;
}
