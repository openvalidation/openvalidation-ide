package de.openvalidation.openvalidationidebackend.ruleset.attribute;

import java.util.Set;

public class AttributeDto {
  private String attributeId;
  private String name;
  private AttributeType attributeType;
  private String value;
  private Set<AttributeDto> children;
}
