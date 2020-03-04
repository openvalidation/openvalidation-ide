package de.openvalidation.openvalidationidebackend.ruleset.attribute;

import de.openvalidation.openvalidationidebackend.ruleset.schema.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Attribute {
  @Id
  private String attributeId;
  private String name;
  private AttributeType attributeType;
  private String value;
  @ManyToOne
  private Schema schema;
  @OneToMany
  private Set<Attribute> children;

  public Attribute() {
    this.attributeId = UUID.randomUUID().toString();
  }
}