package de.openvalidation.openvalidationidebackend.domain.schema.attribute;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
public class AttributeCreateDto {
  @NotNull
  @Size(max = 255)
  private String name;
  @NotNull
  private AttributeType attributeType;
  @Size(max = 255)
  private String value;
  private Set<AttributeCreateDto> children;
}
