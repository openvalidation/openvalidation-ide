package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.core.DtoMapper;
import de.openvalidation.openvalidationidebackend.entities.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeType;
import de.openvalidation.openvalidationidebackend.entities.schema.Schema;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaNotFoundException;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RulesetService {
  private final DtoMapper dtoMapper = Mappers.getMapper(DtoMapper.class);
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;

  @Autowired
  public RulesetService(RulesetRepository rulesetRepository,
                        SchemaRepository schemaRepository) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
  }

  public List<RulesetDto> getAllRulesets() {
    return dtoMapper.toRulesetDtoList(rulesetRepository.findAll());
  }

  public RulesetDto createRuleset(RulesetCreateDto rulesetCreateDto) {
    Schema schema;
    if (rulesetCreateDto.getSchemaId() != null) {
      schema = this.schemaRepository.findById(rulesetCreateDto.getSchemaId()).orElseThrow(SchemaNotFoundException::new);
    } else {
      schema = new Schema();
      Set<Attribute> attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Satoshi", null));
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "25", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema.setAttributes(attributes);
    }
    Ruleset ruleset = dtoMapper.toRulesetEntity(rulesetCreateDto);
    ruleset.setDescription(ruleset.getDescription() != null && ruleset.getDescription().length() > 0
        ? ruleset.getDescription() : ruleset.getName());
    ruleset.setRules("your name HAS to be Validaria");
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
    rulesetRepository.delete(rulesetRepository.findById(rulesetId).orElseThrow(RulesetNotFoundException::new));
  }

}
