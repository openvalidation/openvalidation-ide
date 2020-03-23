package de.openvalidation.openvalidationidebackend.util;

import de.openvalidation.openvalidationidebackend.entities.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeType;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AttributeBuilder {
  private UUID attributeId = UUID.fromString("3e98ccbe-678b-11ea-bc55-0242ac130003");
  private String name = RandomStringUtils.randomAlphabetic(10);
  private AttributeType attributeType = AttributeType.TEXT;
  private String value = "Satoshi";
  private Set<Attribute> children = new HashSet<>();

  public AttributeBuilder setAttributeId(UUID attributeId) {
    this.attributeId = attributeId;
    return this;
  }

  public AttributeBuilder setName(String name) {
    this.name = name;
    return this;
  }

  public AttributeBuilder setAttributeType(AttributeType attributeType) {
    this.attributeType = attributeType;
    return this;
  }

  public AttributeBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  public AttributeBuilder setChildren(Set<Attribute> children) {
    this.children = children;
    return this;
  }

  public Attribute build() {
    return new Attribute(attributeId, name, attributeType, value, children);
  }
}
