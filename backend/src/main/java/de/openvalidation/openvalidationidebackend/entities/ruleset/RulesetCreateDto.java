package de.openvalidation.openvalidationidebackend.entities.ruleset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesetCreateDto {
  @NotNull
  private String name;
  private String description;
  @NotNull
  private String createdBy;
  private UUID schemaId;
}
