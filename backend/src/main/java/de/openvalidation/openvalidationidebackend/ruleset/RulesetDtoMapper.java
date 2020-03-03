package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.ruleset.schema.Schema;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface RulesetDtoMapper {
  Ruleset toRulesetEntity(RulesetDto rulesetDto);
  RulesetDto toRulesetDto(Ruleset ruleset);
  List<Ruleset> toRulesetEntityList(List<RulesetDto> rulesetDtos);
  List<RulesetDto> toRulesetDtoList(List<Ruleset> rulesets);

  Schema toSchemaEntity(SchemaDto schemaDto);
  SchemaDto toSchemaDto(Schema schema);
  Attribute toAttributeEntity(AttributeDto attributeDto);
  AttributeDto toAttributeDto(Attribute attribute);
}
