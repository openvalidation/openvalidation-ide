package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeNotFoundException;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaDto;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaUpdateDto;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RulesetService {
  private final RulesetDtoMapper rulesetDtoMapper = Mappers.getMapper(RulesetDtoMapper.class);
  private RulesetRepository rulesetRepository;

  @Autowired
  public RulesetService(RulesetRepository rulesetRepository) {
    this.rulesetRepository = rulesetRepository;
  }

  /* ###########
     # Ruleset #
     ########### */

  public List<RulesetDto> getAllRulesets() {
    return rulesetDtoMapper.toRulesetDtoList(rulesetRepository.findAll());
  }

  public RulesetDto createRuleset(RulesetCreateDto rulesetCreateDto) {
    return rulesetDtoMapper.toRulesetDto(rulesetRepository.save(rulesetDtoMapper.toRulesetEntity(rulesetCreateDto)));
  }

  public RulesetDto getRuleset(String rulesetId) {
    return rulesetDtoMapper.toRulesetDto(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new));
  }

  public RulesetDto updateRuleset(String rulesetId, RulesetUpdateDto rulesetUpdateDto) {
    return rulesetDtoMapper.toRulesetDto(
        rulesetRepository.save(rulesetRepository.findById(rulesetId)
            .orElseThrow(RulesetNotFoundException::new)
            .updateByRuleset(rulesetDtoMapper.toRulesetEntity(rulesetUpdateDto))));
  }

  public void deleteRuleset(String rulesetId) {
    rulesetRepository.deleteById(rulesetId);
  }

  /* ##########
     # Schema #
     ########## */

  public SchemaDto getSchemaFromRuleset(String rulesetId) {
    return rulesetDtoMapper.toSchemaDto(rulesetRepository.findById(rulesetId).map(Ruleset::getSchema)
        .orElseThrow(RulesetNotFoundException::new));
  }

  public SchemaDto updateSchemaFromRuleset(String rulesetId, SchemaUpdateDto schemaUpdateDto) {
    return rulesetDtoMapper.toSchemaDto(
        rulesetRepository.save(rulesetRepository.findById(rulesetId).map(ruleset -> {
          ruleset.setSchema(ruleset.getSchema().updateBySchema(rulesetDtoMapper.toSchemaEntity(schemaUpdateDto)));
          return ruleset;
        }).orElseThrow(RulesetNotFoundException::new))
            .getSchema());
  }

  /* #############
     # Attribute #
     ############# */

  public Set<AttributeDto> getAllAttributesFromRuleset(String rulesetId) {
    return rulesetDtoMapper.toAttributeDtoSet(rulesetRepository.findById(rulesetId)
        .map(ruleset -> ruleset.getSchema().getAttributes())
        .orElseThrow(RulesetNotFoundException::new));
  }

  public AttributeDto getAttributeFromRuleset(String rulesetId, String attributeId) {
    return rulesetDtoMapper.toAttributeDto(rulesetRepository.findById(rulesetId)
        .map(ruleset -> {
          for (Attribute attribute : ruleset.getSchema().getAttributes()) {
            if (attribute.getAttributeId().equals(attributeId)) {
              return attribute;
            }
          }
          throw new AttributeNotFoundException();
        })
        .orElseThrow(RulesetNotFoundException::new));
  }
}
