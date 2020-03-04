package de.openvalidation.openvalidationidebackend.ruleset;

import de.openvalidation.openvalidationidebackend.ruleset.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaDto;
import de.openvalidation.openvalidationidebackend.ruleset.schema.SchemaUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class RulesetController {
  private RulesetService rulesetService;

  @Autowired
  public RulesetController(RulesetService rulesetService) {
    this.rulesetService = rulesetService;
  }

  /* ###########
     # Ruleset #
     ########### */

  // /rulesets

 @GetMapping(value = "/rulesets",
     produces = MediaType.APPLICATION_JSON_VALUE)
  public List<RulesetDto> getAllRulesets() {
    return rulesetService.getAllRulesets();
  }

  @PostMapping(value = "/rulesets",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public RulesetDto createRuleset(@Valid @RequestBody RulesetCreateDto rulesetCreateDto) {
    return rulesetService.createRuleset(rulesetCreateDto);
  }

  // /rulesets/{rulesetId}

  @GetMapping(value = "/rulesets/{rulesetId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RulesetDto getRuleset(@PathVariable String rulesetId) {
    return rulesetService.getRuleset(rulesetId);
  }

  @PutMapping(value = "/rulesets/{rulesetId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public RulesetDto updateRuleset(@PathVariable String rulesetId,
                                  @Valid @RequestBody RulesetUpdateDto rulesetUpdateDto) {
    return rulesetService.updateRuleset(rulesetId, rulesetUpdateDto);
  }

  @DeleteMapping(value = "/rulesets/{rulesetId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRuleset(@PathVariable String rulesetId) {
    rulesetService.deleteRuleset(rulesetId);
  }

  /* ##########
     # Schema #
     ########## */

  // /rulesets/{rulesetId}/schema

  @GetMapping("/rulesets/{rulesetId}/schema")
  public SchemaDto getSchemaFromRuleset(@PathVariable String rulesetId) {
    return rulesetService.getSchemaFromRuleset(rulesetId);
  }

  @PutMapping(value = "/rulesets/{rulesetId}/schema",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public SchemaDto updateSchema(@PathVariable String rulesetId,
                                @Valid @RequestBody SchemaUpdateDto schemaUpdateDto) {
    return rulesetService.updateSchemaFromRuleset(rulesetId, schemaUpdateDto);
  }

  /* #############
     # Attribute #
     ############# */

  // /rulesets/{rulesetId}/schema/attributes

  @GetMapping("/rulesets/{rulesetId}/schema/attributes")
  public Set<AttributeDto> getAllAttributesFromRuleset(@PathVariable String rulesetId) {
    return rulesetService.getAllAttributesFromRuleset(rulesetId);
  }

  // /rulesets/{rulesetId}/schema/attributes/{attributeId}

  @GetMapping("/rulesets/{rulesetId}/schema/attributes/{attributeId}")
  public AttributeDto getAllAttributesFromRuleset(@PathVariable String rulesetId,
                                                  @PathVariable String attributeId) {
    return rulesetService.getAttributeFromRuleset(rulesetId, attributeId);
  }
}
