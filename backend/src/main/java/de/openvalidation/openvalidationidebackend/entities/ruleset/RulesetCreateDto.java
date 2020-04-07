package de.openvalidation.openvalidationidebackend.entities.ruleset;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RulesetCreateDto {
  @NotBlank
  @Size(max = 255)
  private String name = "Unnamed ruleset";
  @Size(max = 255)
  private String description;
  @NotBlank
  @Size(max = 255)
  private String createdBy = (new Faker()).funnyName().name();
  private UUID schemaId;
}
