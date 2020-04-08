package de.openvalidation.openvalidationidebackend.domain.schema;

import de.openvalidation.openvalidationidebackend.domain.schema.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeNameDuplicateException;
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

  public void setAttributes(Set<Attribute> attributes) {
    Set<String> names = new HashSet<>();
    for (Attribute attribute : attributes) {
      if (names.contains(attribute.getName())) {
        throw new AttributeNameDuplicateException();
      }
      names.add(attribute.getName());
    }
    this.attributes = attributes;
  }

  public Schema updateBySchema(Schema schema) {
    this.setAttributes(schema.attributes != null && !schema.attributes.isEmpty() ? schema.attributes : this.attributes);
    return this;
  }
}
