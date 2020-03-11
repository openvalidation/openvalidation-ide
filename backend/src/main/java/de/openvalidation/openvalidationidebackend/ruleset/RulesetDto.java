package de.openvalidation.openvalidationidebackend.ruleset;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class RulesetDto {
  private UUID rulesetId;
  private String name;
  private String description;
  private Date createdAt;
  private String createdBy;
  private String rules;
  private UUID schemaId;
}
