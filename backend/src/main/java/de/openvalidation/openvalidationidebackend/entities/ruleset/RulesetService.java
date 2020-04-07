package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.core.DtoMapper;
import de.openvalidation.openvalidationidebackend.entities.schema.Schema;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaNotFoundException;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RulesetService {
  private DtoMapper dtoMapper;
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;

  @Autowired
  public RulesetService(RulesetRepository rulesetRepository,
                        SchemaRepository schemaRepository,
                        DtoMapper dtoMapper) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
    this.dtoMapper = dtoMapper;
  }

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
    rulesetRepository.delete(rulesetRepository.findById(rulesetId).orElseThrow(RulesetNotFoundException::new));
  }

}
