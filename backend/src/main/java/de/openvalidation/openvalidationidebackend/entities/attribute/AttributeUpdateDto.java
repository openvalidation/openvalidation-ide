package de.openvalidation.openvalidationidebackend.entities.attribute;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class AttributeUpdateDto {
  private String name;
  @NotNull
  private AttributeType attributeType;
  private String value;
  private Set<AttributeUpdateDto> children;
}
