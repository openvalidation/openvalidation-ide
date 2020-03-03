package de.openvalidation.openvalidationidebackend.ruleset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RulesetController {
  private RulesetService rulesetService;

  @Autowired
  public RulesetController(RulesetService rulesetService) {
    this.rulesetService = rulesetService;
  }

 @GetMapping("/rulesets")
  public List<RulesetDto> getAllRulesets() {
    return rulesetService.getAllRulesets();
  }
}
