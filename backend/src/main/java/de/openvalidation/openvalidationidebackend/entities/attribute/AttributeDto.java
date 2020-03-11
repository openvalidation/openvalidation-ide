package de.openvalidation.openvalidationidebackend.entities.attribute;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class AttributeDto {
  private UUID attributeId;
  private String name;
  private AttributeType attributeType;
  private String value;
  private Set<AttributeDto> children;
}
