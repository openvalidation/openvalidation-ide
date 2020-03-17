package de.openvalidation.openvalidationidebackend.entities.schema;

import de.openvalidation.openvalidationidebackend.entities.attribute.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Schema {
  @Id
  private UUID schemaId;
  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.EAGER)
  private Set<Attribute> attributes;

  public Schema() {
    this.schemaId = UUID.randomUUID();
    this.attributes = new HashSet<>();
  }

  public Schema updateBySchema(Schema schema) {
    this.attributes = !schema.attributes.isEmpty() ? schema.attributes : this.attributes;
    return this;
  }
}
