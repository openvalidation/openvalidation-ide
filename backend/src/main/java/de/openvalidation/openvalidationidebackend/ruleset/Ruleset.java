package de.openvalidation.openvalidationidebackend.ruleset;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Ruleset {
  @Id
  private String rulesetId;
  private String name;
  private String description;
  private Date createdAt;
  private String createdBy;
  private String rules;

  public Ruleset() {
    this.rulesetId = UUID.randomUUID().toString();
  }
}
