package de.openvalidation.openvalidationidebackend.util;

import de.openvalidation.openvalidationidebackend.entities.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.entities.schema.Schema;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SchemaBuilder {
  private UUID schemaId = UUID.fromString("9d203228-678a-11ea-bc55-0242ac130003");
  private Set<Attribute> attributes = new HashSet<>();

  public SchemaBuilder setSchemaId(UUID schemaId) {
    this.schemaId = schemaId;
    return this;
  }

  public SchemaBuilder setAttributes(Set<Attribute> attributes) {
    this.attributes = attributes;
    return this;
  }

  public SchemaBuilder addAttribute(Attribute attribute) {
    attributes.add(attribute);
    return this;
  }

  public Schema build() {
    return new Schema(schemaId, attributes);
  }
}
