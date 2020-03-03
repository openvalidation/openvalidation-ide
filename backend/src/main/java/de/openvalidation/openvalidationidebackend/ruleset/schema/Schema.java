package de.openvalidation.openvalidationidebackend.ruleset.schema;

import de.openvalidation.openvalidationidebackend.ruleset.Ruleset;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.Attribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Schema {
  @Id
  private String schemaId;
  @OneToOne(cascade = CascadeType.ALL)
  private Ruleset ruleset;
  @OneToMany(mappedBy = "schema",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER)
  private Set<Attribute> attributes;

  public Schema() {
    this.schemaId = UUID.randomUUID().toString();
  }
}
