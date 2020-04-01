package de.openvalidation.openvalidationidebackend.entities.ruleset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesetCreateDto {
  @NotNull
  @Size(max = 255)
  private String name;
  @Size(max = 255)
  private String description;
  @NotNull
  @Size(max = 255)
  private String createdBy;
  private UUID schemaId;
}
