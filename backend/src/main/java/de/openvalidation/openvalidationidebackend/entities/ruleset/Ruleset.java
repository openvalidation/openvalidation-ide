package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.entities.schema.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Ruleset {
  @Id
  private UUID rulesetId;
  private String name;
  private String description;
  private Date createdAt;
  private String createdBy;
  private Date lastEdit;
  @Column(columnDefinition = "text")
  private String rules;
  @ManyToOne
  private Schema schema;

  public Ruleset() {
    this.rulesetId = UUID.randomUUID();
    this.createdAt = new Date();
    this.schema = new Schema();
    this.lastEdit = new Date();
  }

  public Ruleset updateByRuleset(Ruleset ruleset) {
    this.name = ruleset.name != null ? ruleset.name : this.name;
    this.description = ruleset.description != null ? ruleset.description : this.description;
    this.rules = ruleset.rules != null ? ruleset.rules : this.rules;
    this.schema = !ruleset.schema.getAttributes().isEmpty() ? ruleset.schema : this.schema;
    this.lastEdit = new Date();
    return this;
  }
}
