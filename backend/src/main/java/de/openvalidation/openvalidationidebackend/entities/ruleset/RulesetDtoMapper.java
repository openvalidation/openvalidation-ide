package de.openvalidation.openvalidationidebackend.entities.ruleset;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RulesetDtoMapper {
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
}
