package de.openvalidation.openvalidationidebackend.ruleset.attribute;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class AttributeDto {
  private String attributeId;
  private String name;
  private AttributeType attributeType;
  private String value;
  private Set<AttributeDto> children;
}
