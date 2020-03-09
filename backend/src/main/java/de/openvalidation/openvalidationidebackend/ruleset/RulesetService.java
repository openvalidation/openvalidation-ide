package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.*;
import de.openvalidation.openvalidationidebackend.ruleset.schema.Schema;
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

  public AttributeDto createAttributeFromRuleset(String rulesetId, AttributeCreateDto attributeCreateDto) {
    Attribute createdAttribute = rulesetDtoMapper.toAttributeEntity(attributeCreateDto);
    return rulesetDtoMapper.toAttributeDto(
        rulesetRepository.save(rulesetRepository.findById(rulesetId).map(ruleset -> {
          Schema schema = ruleset.getSchema();
          Set<Attribute> attributes = schema.getAttributes();
          attributes.add(createdAttribute);
          schema.setAttributes(attributes);
          ruleset.setSchema(schema);
          return ruleset;
        }).orElseThrow(RulesetNotFoundException::new))
            .getSchema().getAttributes().parallelStream().filter(attribute -> attribute.getAttributeId().equals(createdAttribute.getAttributeId()))
            .findFirst().orElseThrow(AttributeNotFoundException::new));
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
        }).orElseThrow(RulesetNotFoundException::new));
  }

  public AttributeDto updateAttributeFromRuleset(String rulesetId, String attributeId, AttributeUpdateDto attributeUpdateDto) {
    return rulesetDtoMapper.toAttributeDto(
        rulesetRepository.save(rulesetRepository.findById(rulesetId).map(ruleset -> {
          Schema schema = ruleset.getSchema();
          Set<Attribute> attributes = schema.getAttributes();
          Attribute updatedAttribute = attributes.parallelStream().filter(attribute -> attribute.getAttributeId().equals(attributeId))
              .findFirst().orElseThrow(AttributeNotFoundException::new);
          attributes.removeIf(attribute -> attribute.getAttributeId().equals(attributeId));
          attributes.add(updatedAttribute.updateByAttribute(rulesetDtoMapper.toAttributeEntity(attributeUpdateDto)));
          schema.setAttributes(attributes);
          ruleset.setSchema(schema);
          return ruleset;
        }).orElseThrow(RulesetNotFoundException::new))
            .getSchema().getAttributes().parallelStream().filter(attribute -> attribute.getAttributeId().equals(attributeId))
            .findFirst().orElseThrow(AttributeNotFoundException::new));
  }

  public void deleteAttributeFromRuleset(String rulesetId, String attributeId) {
    rulesetRepository.save(rulesetRepository.findById(rulesetId).map(ruleset -> {
      Schema schema = ruleset.getSchema();
      Set<Attribute> attributes = schema.getAttributes();
      attributes.removeIf(attribute -> attribute.getAttributeId().equals(attributeId));
      schema.setAttributes(attributes);
      ruleset.setSchema(schema);
      return ruleset;
    }).orElseThrow(RulesetNotFoundException::new));
  }
}
