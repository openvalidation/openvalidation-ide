package de.openvalidation.openvalidationidebackend.ruleset.attribute;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.HashSet;
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
  @OneToMany
  private Set<Attribute> children;

  public Attribute() {
    this.attributeId = UUID.randomUUID().toString();
    this.children = new HashSet<>();
  }

  public Attribute updateByAttribute(Attribute attribute) {
    this.name = attribute.name != null ? attribute.name : this.name;
    this.attributeType = attribute.attributeType != null ? attribute.attributeType : this.attributeType;
    this.value = attribute.value != null ? attribute.value : this.value;
    this.children = attribute.children != null && !attribute.children.isEmpty() ? attribute.children : this.children;
    return this;
  }
}
