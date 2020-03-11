package de.openvalidation.openvalidationidebackend.ruleset;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
public class RulesetCreateDto {
  @NotNull
  private String name;
  private String description;
  @NotNull
  private String createdBy;
  private UUID schemaId;
}
