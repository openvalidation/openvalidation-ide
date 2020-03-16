package de.openvalidation.openvalidationidebackend.entities.attribute;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Attribute {
  @Id
  private UUID attributeId;
  private String name;
  private AttributeType attributeType;
  private String value;
  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.EAGER)
  private Set<Attribute> children;

  public Attribute() {
    this.attributeId = UUID.randomUUID();
  }

  public void setAttributeType(AttributeType attributeType) {
    this.attributeType = attributeType;
    if (attributeType != AttributeType.OBJECT && attributeType != AttributeType.LIST) {
      this.children = null;
    } else {
      this.value = null;
    }
  }

  public void setValue(String value) {
    if (attributeType != AttributeType.OBJECT && attributeType != AttributeType.LIST) {
      this.value = value;
    }
  }

  public void setChildren(Set<Attribute> children) {
    if (this.attributeType == AttributeType.OBJECT || this.attributeType == AttributeType.LIST) {
      this.children = children;
    }
  }

  public Attribute updateByAttribute(Attribute attribute) {
    this.name = attribute.name != null ? attribute.name : this.name;
    this.setAttributeType(attribute.attributeType != null ? attribute.attributeType : this.attributeType);
    this.value = attribute.value != null ? attribute.value : this.value;
    this.setChildren(attribute.children != null && !attribute.children.isEmpty() ? attribute.children : this.children);
    return this;
  }
}
