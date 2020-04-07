package de.openvalidation.openvalidationidebackend.entities.attribute;

import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface AttributeDtoMapper {
  Attribute toAttributeEntity(AttributeDto attributeDto);
  Attribute toAttributeEntity(AttributeCreateDto attributeCreateDto);
  Attribute toAttributeEntity(AttributeUpdateDto attributeUpdateDto);
  AttributeDto toAttributeDto(Attribute attribute);
  Set<Attribute> toAttributeEntitySet(Set<AttributeDto> attributeDtos);
  Set<AttributeDto> toAttributeDtoSet(Set<Attribute> attributes);
}
