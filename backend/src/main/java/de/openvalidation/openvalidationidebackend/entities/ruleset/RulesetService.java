package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.entities.schema.Schema;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaNotFoundException;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
    return rulesetDtoMapper.toRulesetDtoList(rulesetRepository.findAll());
  }

  public RulesetDto createRuleset(RulesetCreateDto rulesetCreateDto) {
    Schema schema = rulesetCreateDto.getSchemaId() != null
        ? this.schemaRepository.findById(rulesetCreateDto.getSchemaId()).orElseThrow(SchemaNotFoundException::new)
        : new Schema();
    Ruleset ruleset = rulesetDtoMapper.toRulesetEntity(rulesetCreateDto);
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
