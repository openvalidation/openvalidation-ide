package de.openvalidation.openvalidationidebackend.domain.ruleset;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
public class RulesetController {
  private RulesetService rulesetService;

  @Autowired
  public RulesetController(RulesetService rulesetService) {
    this.rulesetService = rulesetService;
  }

  @Tag(name = "rulesets")
  @GetMapping(value = "/rulesets",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<RulesetDto> getAllRulesets() {
    return rulesetService.getAllRulesets();
  }

  @Tag(name = "rulesets")
  @PostMapping(value = "/rulesets",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public RulesetDto createRuleset(@Valid @RequestBody(required = false) RulesetCreateDto rulesetCreateDto) {
    return rulesetService.createRuleset(rulesetCreateDto == null ? new RulesetCreateDto() : rulesetCreateDto);
  }

  @Tag(name = "rulesets")
  @GetMapping(value = "/rulesets/{rulesetId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RulesetDto getRuleset(@PathVariable UUID rulesetId) {
    return rulesetService.getRuleset(rulesetId);
  }

  @Tag(name = "rulesets")
  @PutMapping(value = "/rulesets/{rulesetId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RulesetDto updateRuleset(@PathVariable UUID rulesetId,
                                  @Valid @RequestBody RulesetUpdateDto rulesetUpdateDto) {
    return rulesetService.updateRuleset(rulesetId, rulesetUpdateDto);
  }

  @Tag(name = "rulesets")
  @DeleteMapping(value = "/rulesets/{rulesetId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRuleset(@PathVariable UUID rulesetId) {
    rulesetService.deleteRuleset(rulesetId);
  }

}
