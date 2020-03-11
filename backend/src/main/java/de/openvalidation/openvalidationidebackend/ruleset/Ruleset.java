package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.schema.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Ruleset {
  @Id
  private UUID rulesetId;
  private String name;
  private String description;
  private Date createdAt;
  private String createdBy;
  private String rules;
  @ManyToOne
  private Schema schema;

  public Ruleset() {
    this.rulesetId = UUID.randomUUID();
    this.createdAt = new Date();
    this.schema = new Schema();
  }

  public Ruleset updateByRuleset(Ruleset ruleset) {
    this.name = ruleset.name != null ? ruleset.name : this.name;
    this.description = ruleset.description != null ? ruleset.description : this.description;
    this.rules = ruleset.rules != null ? ruleset.rules : this.rules;
    this.schema = !ruleset.schema.getAttributes().isEmpty() ? ruleset.schema : this.schema;
    return this;
  }
}
