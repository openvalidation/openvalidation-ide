package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeCreateDto;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeUpdateDto;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaDto;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@CrossOrigin
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
  public RulesetDto createRuleset(@Valid @RequestBody RulesetCreateDto rulesetCreateDto) {
    return rulesetService.createRuleset(rulesetCreateDto);
  }

  // /rulesets/{rulesetId}

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

  /* ##########
     # Schema #
     ########## */

  // /rulesets/{rulesetId}/schema

  @Tag(name = "schema")
  @GetMapping(value = "/rulesets/{rulesetId}/schema")
  public SchemaDto getSchemaFromRuleset(@PathVariable UUID rulesetId) {
    return rulesetService.getSchemaFromRuleset(rulesetId);
  }

  @Tag(name = "schema")
  @PutMapping(value = "/rulesets/{rulesetId}/schema",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public SchemaDto updateSchema(@PathVariable UUID rulesetId,
                                @Valid @RequestBody SchemaUpdateDto schemaUpdateDto) {
    return rulesetService.updateSchemaFromRuleset(rulesetId, schemaUpdateDto);
  }

  /* #############
     # Attribute #
     ############# */

  // /rulesets/{rulesetId}/schema/attributes

  @Tag(name = "attributes")
  @GetMapping(value = "/rulesets/{rulesetId}/schema/attributes")
  public Set<AttributeDto> getAllAttributesFromRuleset(@PathVariable UUID rulesetId) {
    return rulesetService.getAllAttributesFromRuleset(rulesetId);
  }

  @Tag(name = "attributes")
  @PostMapping(value = "/rulesets/{rulesetId}/schema/attributes",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Set<AttributeDto> createAttributesFromRuleset(@PathVariable UUID rulesetId,
                                                 @Valid @RequestBody Set<AttributeCreateDto> attributeCreateDtos) {
    return rulesetService.createAttributesFromRuleset(rulesetId, attributeCreateDtos);
  }

  // /rulesets/{rulesetId}/schema/attributes/{attributeId}

  @Tag(name = "attributes")
  @GetMapping(value = "/rulesets/{rulesetId}/schema/attributes/{attributeId}")
  public AttributeDto getAllAttributesFromRuleset(@PathVariable UUID rulesetId,
                                                  @PathVariable UUID attributeId) {
    return rulesetService.getAttributeFromRuleset(rulesetId, attributeId);
  }

  @Tag(name = "attributes")
  @PutMapping(value = "/rulesets/{rulesetId}/schema/attributes/{attributeId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttributeDto updateAttributeFromRuleset(@PathVariable UUID rulesetId,
                                                 @PathVariable UUID attributeId,
                                                 @Valid @RequestBody AttributeUpdateDto attributeUpdateDto) {
    return rulesetService.updateAttributeFromRuleset(rulesetId, attributeId, attributeUpdateDto);
  }

  @Tag(name = "attributes")
  @DeleteMapping(value = "/rulesets/{rulesetId}/schema/attributes/{attributeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAttributeFromRuleset(@PathVariable UUID rulesetId,
                                         @PathVariable UUID attributeId) {
    rulesetService.deleteAttributeFromRuleset(rulesetId, attributeId);
  }
}
