package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.core.DtoMapper;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeCreateDto;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeUpdateDto;
import de.openvalidation.openvalidationidebackend.entities.schema.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RulesetService {
  private final DtoMapper dtoMapper = Mappers.getMapper(DtoMapper.class);
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;
  private SchemaService schemaService;

  @Autowired
  public RulesetService(RulesetRepository rulesetRepository,
                        SchemaRepository schemaRepository,
                        SchemaService schemaService) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
    this.schemaService = schemaService;
  }

  /* ###########
     # Ruleset #
     ########### */

  public List<RulesetDto> getAllRulesets() {
    return dtoMapper.toRulesetDtoList(rulesetRepository.findAll());
  }

  public RulesetDto createRuleset(RulesetCreateDto rulesetCreateDto) {
    Schema schema = rulesetCreateDto.getSchemaId() != null
        ? this.schemaRepository.findById(rulesetCreateDto.getSchemaId()).orElseThrow(SchemaNotFoundException::new)
        : new Schema();
    Ruleset ruleset = dtoMapper.toRulesetEntity(rulesetCreateDto);
    ruleset.setSchema(this.schemaRepository.save(schema));
    return dtoMapper.toRulesetDto(rulesetRepository.save(ruleset));
  }

  public RulesetDto getRuleset(UUID rulesetId) {
    return dtoMapper.toRulesetDto(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new));
  }

  public RulesetDto updateRuleset(UUID rulesetId, RulesetUpdateDto rulesetUpdateDto) {
    return dtoMapper.toRulesetDto(
        rulesetRepository.save(rulesetRepository.findById(rulesetId)
            .orElseThrow(RulesetNotFoundException::new)
            .updateByRuleset(dtoMapper.toRulesetEntity(rulesetUpdateDto))));
  }

  public void deleteRuleset(UUID rulesetId) {
    rulesetRepository.deleteById(rulesetId);
  }

  /* ##########
     # Schema #
     ########## */

  public SchemaDto getSchemaFromRuleset(UUID rulesetId) {
    return dtoMapper.toSchemaDto(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema());
  }

  public SchemaDto updateSchemaFromRuleset(UUID rulesetId, SchemaUpdateDto schemaUpdateDto) {
    return this.schemaService.updateSchema(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema().getSchemaId(), schemaUpdateDto);
  }

  /* #############
     # Attribute #
     ############# */

  public Set<AttributeDto> getAllAttributesFromRuleset(UUID rulesetId) {
    return this.schemaService.getAllAttributesFromSchema(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema().getSchemaId());
  }

  public AttributeDto createAttributeFromRuleset(UUID rulesetId, AttributeCreateDto attributeCreateDto) {
    return this.schemaService.createAttributeFromSchema(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema().getSchemaId(), attributeCreateDto);
  }

  public AttributeDto getAttributeFromRuleset(UUID rulesetId, UUID attributeId) {
    return this.schemaService.getAttributeFromSchema(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema().getSchemaId(), attributeId);
  }

  public AttributeDto updateAttributeFromRuleset(UUID rulesetId, UUID attributeId, AttributeUpdateDto attributeUpdateDto) {
    return this.schemaService.updateAttributeFromSchema(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema().getSchemaId(), attributeId, attributeUpdateDto);
  }

  public void deleteAttributeFromRuleset(UUID rulesetId, UUID attributeId) {
    this.schemaService.deleteAttributeFromSchema(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new).getSchema().getSchemaId(), attributeId);
  }
}
