package de.openvalidation.openvalidationidebackend.ruleset.schema;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.Attribute;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Schema {
  @Id
  private String schemaId;
  @OneToMany(cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.EAGER)
  private Set<Attribute> attributes;

  public Schema() {
    this.schemaId = UUID.randomUUID().toString();
    this.attributes = new HashSet<>();
  }

  public Schema updateBySchema(Schema schema) {
    this.attributes = !schema.attributes.isEmpty() ? schema.attributes : this.attributes;
    return this;
  }
}
