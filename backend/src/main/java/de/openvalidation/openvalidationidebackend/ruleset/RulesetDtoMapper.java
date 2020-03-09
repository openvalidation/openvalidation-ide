package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeCreateDto;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeUpdateDto;
import de.openvalidation.openvalidationidebackend.ruleset.schema.Schema;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaDto;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaUpdateDto;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface RulesetDtoMapper {
  Ruleset toRulesetEntity(RulesetDto rulesetDto);
  Ruleset toRulesetEntity(RulesetCreateDto rulesetCreateDto);
  Ruleset toRulesetEntity(RulesetUpdateDto rulesetUpdateDto);
  RulesetDto toRulesetDto(Ruleset ruleset);
  List<Ruleset> toRulesetEntityList(List<RulesetDto> rulesetDtos);
  List<RulesetDto> toRulesetDtoList(List<Ruleset> rulesets);

  Schema toSchemaEntity(SchemaDto schemaDto);
  Schema toSchemaEntity(SchemaUpdateDto schemaUpdateDto);
  SchemaDto toSchemaDto(Schema schema);

  Attribute toAttributeEntity(AttributeDto attributeDto);
  Attribute toAttributeEntity(AttributeCreateDto attributeCreateDto);
  Attribute toAttributeEntity(AttributeUpdateDto attributeUpdateDto);
  AttributeDto toAttributeDto(Attribute attribute);
  Set<Attribute> toAttributeEntitySet(Set<AttributeDto> attributeDtos);
  Set<AttributeDto> toAttributeDtoSet(Set<Attribute> attributes);
}
