package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaDto;

import java.util.Date;

public class RulesetDto {
  private String rulesetId;
  private String name;
  private String description;
  private Date createdAt;
  private String createdBy;
  private String rules;
  private SchemaDto schema;
}
