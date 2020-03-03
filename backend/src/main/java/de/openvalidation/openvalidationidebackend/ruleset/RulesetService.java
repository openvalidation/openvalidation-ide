package de.openvalidation.openvalidationidebackend.ruleset;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RulesetService {
  private final RulesetDtoMapper rulesetDtoMapper = Mappers.getMapper(RulesetDtoMapper.class);
  private RulesetRepository rulesetRepository;

  @Autowired
  public RulesetService(RulesetRepository rulesetRepository) {
    this.rulesetRepository = rulesetRepository;
  }

  public List<RulesetDto> getAllRulesets() {
    return rulesetDtoMapper.toRulesetDtoList(rulesetRepository.findAll());
  }
}
