package de.openvalidation.openvalidationidebackend.core;

import de.openvalidation.openvalidationidebackend.entities.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeCreateDto;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeUpdateDto;
import de.openvalidation.openvalidationidebackend.entities.ruleset.Ruleset;
import de.openvalidation.openvalidationidebackend.entities.ruleset.RulesetCreateDto;
import de.openvalidation.openvalidationidebackend.entities.ruleset.RulesetDto;
import de.openvalidation.openvalidationidebackend.entities.ruleset.RulesetUpdateDto;
import de.openvalidation.openvalidationidebackend.entities.schema.Schema;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaDto;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaUpdateDto;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface DtoMapper {
  Ruleset toRulesetEntity(RulesetDto rulesetDto);
  Ruleset toRulesetEntity(RulesetCreateDto rulesetCreateDto);
  Ruleset toRulesetEntity(RulesetUpdateDto rulesetUpdateDto);
  default RulesetDto toRulesetDto(Ruleset ruleset) {
    if (ruleset == null) {
      return null;
    } else {
      RulesetDto rulesetDto = new RulesetDto();
      rulesetDto.setRulesetId(ruleset.getRulesetId());
      rulesetDto.setName(ruleset.getName());
      rulesetDto.setDescription(ruleset.getDescription());
      rulesetDto.setCreatedAt(ruleset.getCreatedAt());
      rulesetDto.setCreatedBy(ruleset.getCreatedBy());
      rulesetDto.setRules(ruleset.getRules());
      rulesetDto.setSchemaId(ruleset.getSchema().getSchemaId());
      return rulesetDto;
    }
  }
  List<RulesetDto> toRulesetDtoList(List<Ruleset> rulesets);

  Schema toSchemaEntity(SchemaUpdateDto schemaUpdateDto);
  SchemaDto toSchemaDto(Schema schema);
  List<SchemaDto> toSchemaDtoList(List<Schema> schemas);

  Attribute toAttributeEntity(AttributeDto attributeDto);
  Attribute toAttributeEntity(AttributeCreateDto attributeCreateDto);
  Attribute toAttributeEntity(AttributeUpdateDto attributeUpdateDto);
  AttributeDto toAttributeDto(Attribute attribute);
  Set<Attribute> toAttributeEntitySet(Set<AttributeDto> attributeDtos);
  Set<AttributeDto> toAttributeDtoSet(Set<Attribute> attributes);
}
