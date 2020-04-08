package de.openvalidation.openvalidationidebackend.domain.schema;

import de.openvalidation.openvalidationidebackend.domain.schema.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SchemaBuilder {
  private UUID schemaId = UUID.fromString("9d203228-678a-11ea-bc55-0242ac130003");
  private Set<Attribute> attributes = new HashSet<>();

  public SchemaBuilder() {
    attributes.add(new AttributeBuilder()
        .setAttributeId(UUID.fromString("3e98ccbe-678b-11ea-bc55-0242ac130003"))
        .setName("name")
        .setAttributeType(AttributeType.TEXT)
        .setValue("Satoshi")
        .build());

    attributes.add(new AttributeBuilder()
        .setAttributeId(UUID.fromString("97aa6d3d-8274-4093-b24a-35eeebc588e8"))
        .setName("age")
        .setAttributeType(AttributeType.NUMBER)
        .setValue("25")
        .build());

    attributes.add(new AttributeBuilder()
        .setAttributeId(UUID.fromString("30f8734b-cd81-4725-a3d6-8d9ef2755795"))
        .setName("Has a car")
        .setAttributeType(AttributeType.BOOLEAN)
        .setValue("true")
        .build());
  }

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
