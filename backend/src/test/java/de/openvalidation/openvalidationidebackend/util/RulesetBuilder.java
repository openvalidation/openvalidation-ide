package de.openvalidation.openvalidationidebackend.util;

import de.openvalidation.openvalidationidebackend.entities.ruleset.Ruleset;
import de.openvalidation.openvalidationidebackend.entities.schema.Schema;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RulesetBuilder {
  private UUID rulesetId = UUID.fromString("d0d95814-678b-11ea-bc55-0242ac130003");
  private String name = "Name validation";
  private String description = "Validating the name";
  private Date createdAt = Calendar.getInstance().getTime();
  private String createdBy = "Validaria";
  private String rules = "your name HAS to be Validaria";
  private Schema schema = new SchemaBuilder().addAttribute(new AttributeBuilder().build()).build();

  public RulesetBuilder setRulesetId(UUID rulesetId) {
    this.rulesetId = rulesetId;
    return this;
  }

  public RulesetBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public RulesetBuilder setDescription(String description) {
    this.description = description;
    return this;
  }

  public RulesetBuilder setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public RulesetBuilder setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public RulesetBuilder setRules(String rules) {
    this.rules = rules;
    return this;
  }

  public RulesetBuilder setSchema(Schema schema) {
    this.schema = schema;
    return this;
  }

  public Ruleset build() {
    return new Ruleset(rulesetId, name, description, createdAt, createdBy, rules, schema);
  }

}
