package de.openvalidation.openvalidationidebackend.domain.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeCreateDto;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeDto;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeUpdateDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
public class SchemaController {
  private SchemaService schemaService;

  @Autowired
  public SchemaController(SchemaService schemaService) {
    this.schemaService = schemaService;
  }

  /* ##########
     # Schema #
     ########## */

  // /schema

  @Tag(name = "schema")
  @GetMapping(value = "/schemas")
  public List<SchemaDto> getAllSchemas() {
    return this.schemaService.getAllSchemas();
  }

  // /schema/{schemaId}

  @Tag(name = "schema")
  @GetMapping(value = "/schemas/{schemaId}")
  public SchemaDto getSchema(@PathVariable UUID schemaId) {
    return this.schemaService.getSchema(schemaId);
  }

  @Tag(name = "schema")
  @PutMapping(value = "/schemas/{schemaId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public SchemaDto updateSchema(@PathVariable UUID schemaId,
                                @Valid @RequestBody SchemaUpdateDto schemaUpdateDto) {
    return this.schemaService.updateSchema(schemaId, schemaUpdateDto);
  }

  /* #############
     # Attribute #
     ############# */

  // /schema/{schemaId}/attributes

  @Tag(name = "attributes")
  @GetMapping(value = "/schemas/{schemaId}/attributes")
  public Set<AttributeDto> getAllAttributesFromSchema(@PathVariable UUID schemaId) {
    return this.schemaService.getAllAttributesFromSchema(schemaId);
  }

  @Tag(name = "attributes")
  @PostMapping(value = "/schemas/{schemaId}/attributes",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public Set<AttributeDto> addAttributesToSchema(@PathVariable UUID schemaId,
                                                 @Valid @RequestBody Set<AttributeCreateDto> attributeCreateDtos) {
    return this.schemaService.addAttributesToSchema(schemaId, attributeCreateDtos);
  }

  // /schema/{schemaId}/attributes/{attributeId}

  @Tag(name = "attributes")
  @GetMapping(value = "/schemas/{schemaId}/attributes/{attributeId}")
  public AttributeDto getAttributeFromSchema(@PathVariable UUID schemaId,
                                             @PathVariable UUID attributeId) {
    return this.schemaService.getAttributeFromSchema(schemaId, attributeId);
  }

  @Tag(name = "attributes")
  @PutMapping(value = "/schemas/{schemaId}/attributes/{attributeId}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public AttributeDto updateAttributeFromSchema(@PathVariable UUID schemaId,
                                                @PathVariable UUID attributeId,
                                                @Valid @RequestBody AttributeUpdateDto attributeUpdateDto) {
    return this.schemaService.updateAttributeFromSchema(schemaId, attributeId, attributeUpdateDto);
  }

  @Tag(name = "attributes")
  @DeleteMapping(value = "/schemas/{schemaId}/attributes/{attributeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAttributeFromSchema(@PathVariable UUID schemaId,
                                        @PathVariable UUID attributeId) {
    this.schemaService.deleteAttributeFromSchema(schemaId, attributeId);
  }

  // /schema/{schemaId}/export

  @Tag(name = "schema")
  @GetMapping(value = "/schemas/{schemaId}/export",
      produces = {MediaType.APPLICATION_JSON_VALUE, "application/x-yaml"})
  public String exportSchema(@RequestHeader(HttpHeaders.ACCEPT) String acceptHeader,
                             @PathVariable UUID schemaId) throws JsonProcessingException {
    return this.schemaService.exportSchema(schemaId,
        !acceptHeader.isEmpty() ? acceptHeader : MediaType.APPLICATION_JSON_VALUE);
  }
}
