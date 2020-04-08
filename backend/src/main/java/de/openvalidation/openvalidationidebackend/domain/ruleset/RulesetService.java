package de.openvalidation.openvalidationidebackend.domain.ruleset;

import de.openvalidation.openvalidationidebackend.domain.schema.Schema;
import de.openvalidation.openvalidationidebackend.domain.schema.SchemaNotFoundException;
import de.openvalidation.openvalidationidebackend.domain.schema.SchemaRepository;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RulesetService {
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;
  private RulesetDtoMapper rulesetDtoMapper;

  @Autowired
  public RulesetService(RulesetRepository rulesetRepository,
                        SchemaRepository schemaRepository,
                        RulesetDtoMapper rulesetDtoMapper) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
    this.rulesetDtoMapper = rulesetDtoMapper;
  }

  public List<RulesetDto> getAllRulesets() {
    return rulesetDtoMapper.toRulesetDtoList(rulesetRepository.findAll(Sort.by(Sort.Direction.DESC, "lastEdit")));
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
    Ruleset ruleset = rulesetDtoMapper.toRulesetEntity(rulesetCreateDto);
    ruleset.setRules("your name HAS to be Validaria");
    ruleset.setSchema(this.schemaRepository.save(schema));
    return rulesetDtoMapper.toRulesetDto(rulesetRepository.save(ruleset));
  }

  public RulesetDto getRuleset(UUID rulesetId) {
    return rulesetDtoMapper.toRulesetDto(rulesetRepository.findById(rulesetId)
        .orElseThrow(RulesetNotFoundException::new));
  }

  public RulesetDto updateRuleset(UUID rulesetId, RulesetUpdateDto rulesetUpdateDto) {
    return rulesetDtoMapper.toRulesetDto(
        rulesetRepository.save(rulesetRepository.findById(rulesetId)
            .orElseThrow(RulesetNotFoundException::new)
            .updateByRuleset(rulesetDtoMapper.toRulesetEntity(rulesetUpdateDto))));
  }

  public void deleteRuleset(UUID rulesetId) {
    rulesetRepository.delete(rulesetRepository.findById(rulesetId).orElseThrow(RulesetNotFoundException::new));
  }

}
