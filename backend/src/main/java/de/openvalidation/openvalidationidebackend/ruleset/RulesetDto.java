package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RulesetDto {
  private String rulesetId;
  private String name;
  private String description;
  private Date createdAt;
  private String createdBy;
  private String rules;
  private SchemaDto schema;
}
