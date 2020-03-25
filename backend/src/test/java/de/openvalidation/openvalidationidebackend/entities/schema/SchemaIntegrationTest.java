package de.openvalidation.openvalidationidebackend.entities.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.openvalidation.openvalidationidebackend.entities.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.entities.attribute.AttributeType;
import de.openvalidation.openvalidationidebackend.entities.ruleset.RulesetRepository;
import de.openvalidation.openvalidationidebackend.util.AttributeBuilder;
import de.openvalidation.openvalidationidebackend.util.SchemaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsIn.in;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SchemaIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RulesetRepository rulesetRepository;
  @Autowired
  private SchemaRepository schemaRepository;

  @BeforeEach
  public void setUp() {
    rulesetRepository.deleteAll();
    schemaRepository.deleteAll();
    schemaRepository.save(new SchemaBuilder().build());
  }

  @AfterEach
  public void clearRepository() {
    schemaRepository.deleteAll();
  }

  @Test
  public void onGetAll_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(get("/schemas")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].schemaId", is(schemaId)));
  }

  @Test
  public void whenValidId_onGet_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(get("/schemas/" + schemaId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.schemaId", is(schemaId)));
  }

  @Test
  public void whenInvalidId_onGet_thenStatusClientError() throws Exception {
    mockMvc.perform(get("/schemas/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidId_onUpdate_thenStatusSuccessful() throws Exception {
    Schema updateSchema = new SchemaBuilder().build();

    mockMvc.perform(put("/schemas/" + updateSchema.getSchemaId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(updateSchema)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.schemaId", is(updateSchema.getSchemaId().toString())));
  }

  @Test
  public void whenInvalidId_onUpdate_thenStatusClientError() throws Exception {
    Schema updateSchema = new SchemaBuilder().build();

    mockMvc.perform(put("/schemas/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(updateSchema)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidId_onGetAllAttributes_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();
    Collection<String> attributeIds = new ArrayList<>();
    for (Attribute attribute : new SchemaBuilder().build().getAttributes()) {
      attributeIds.add(attribute.getAttributeId().toString());
    }

    mockMvc.perform(get("/schemas/" + schemaId + "/attributes")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].attributeId", in(attributeIds)));
  }

  @Test
  public void whenInvalidId_onGetAllAttributes_thenStatusClientError() throws Exception {
    mockMvc.perform(get("/schemas/" + UUID.randomUUID().toString() + "/attributes")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidId_onCreateAttributes_thenStatusSuccessful() throws Exception {
    Set<Attribute> createAttributes = new HashSet<>();
    createAttributes.add(new AttributeBuilder().setAttributeId(UUID.randomUUID()).build());
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(post("/schemas/" + schemaId + "/attributes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(createAttributes)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    Set<Attribute> foundAttributes = schemaRepository.findById(new SchemaBuilder().build().getSchemaId())
        .orElseThrow(() -> new Exception("Could not find Schema")).getAttributes();
    assertThat(foundAttributes).hasSizeGreaterThan(new SchemaBuilder().build().getAttributes().size());
  }

  @Test
  public void whenInvalidId_onCreateAttributes_thenStatusClientError() throws Exception {
    Set<Attribute> createAttributes = new HashSet<>();
    createAttributes.add(new AttributeBuilder().build());

    mockMvc.perform(post("/schemas/" + UUID.randomUUID().toString() + "/attributes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(createAttributes)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenDuplicateAttributeName_onCreateAttributes_thenStatusClientError() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();
    Set<Attribute> createAttributes = new HashSet<>();
    createAttributes.add(new AttributeBuilder().setName("dup").build());
    createAttributes.add(new AttributeBuilder().setName("dup").build());

    mockMvc.perform(post("/schemas/" + schemaId + "/attributes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(createAttributes)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenInvalidValue_onCreateAttributes_thenStatusClientError() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();
    Set<Attribute> createAttributes = new HashSet<>();
    createAttributes.add(new AttributeBuilder().setAttributeType(AttributeType.NUMBER).setValue("Test_NaN").build());

    mockMvc.perform(post("/schemas/" + schemaId + "/attributes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(createAttributes)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidIds_onGetAttribute_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();
    String attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId().toString();

    mockMvc.perform(get("/schemas/" + schemaId + "/attributes/" + attributeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.attributeId", is(attributeId)));
  }

  @Test
  public void whenInvalidSchemaId_onGetAttribute_thenStatusClientError() throws Exception {
    String attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId().toString();

    mockMvc.perform(get("/schemas/" + UUID.randomUUID().toString() + "/attributes/" + attributeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenInvalidAttributeId_onGetAttribute_thenStatusClientError() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(get("/schemas/" + schemaId + "/attributes/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidIds_onUpdateAttribute_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();
    String attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId().toString();

    mockMvc.perform(put("/schemas/" + schemaId + "/attributes/" + attributeId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(new AttributeBuilder().build())))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.attributeId", is(attributeId)));
  }

  @Test
  public void whenInvalidSchemaId_onUpdateAttribute_thenStatusClientError() throws Exception {
    String attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId().toString();

    mockMvc.perform(put("/schemas/" + UUID.randomUUID().toString() + "/attributes/" + attributeId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(new AttributeBuilder().build())))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenInvalidAttributeId_onUpdateAttribute_thenStatusClientError() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(put("/schemas/" + schemaId + "/attributes/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(new AttributeBuilder().build())))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidIds_onDeleteAttribute_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();
    String attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId().toString();

    mockMvc.perform(delete("/schemas/" + schemaId + "/attributes/" + attributeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    Set<Attribute> foundAttributes = schemaRepository.findById(new SchemaBuilder().build().getSchemaId())
        .orElseThrow(() -> new Exception("Could not find Schema")).getAttributes();
    assertThat(foundAttributes).hasSizeLessThan(new SchemaBuilder().build().getAttributes().size());
  }

  @Test
  public void whenInvalidSchemaId_onDeleteAttribute_thenStatusClientError() throws Exception {
    String attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId().toString();

    mockMvc.perform(delete("/schemas/" + UUID.randomUUID().toString() + "/attributes/" + attributeId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenInvalidAttributeId_onDeleteAttribute_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(delete("/schemas/" + schemaId + "/attributes/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());

    Set<Attribute> foundAttributes = schemaRepository.findById(new SchemaBuilder().build().getSchemaId())
        .orElseThrow(() -> new Exception("Could not find Schema")).getAttributes();
    assertThat(foundAttributes).hasSameSizeAs(new SchemaBuilder().build().getAttributes());
  }

  @Test
  public void whenValidSchemaId_onExportSchemaJson_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(get("/schemas/" + schemaId + "/export")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void whenValidSchemaId_onExportSchemaYaml_thenStatusSuccessful() throws Exception {
    String schemaId = new SchemaBuilder().build().getSchemaId().toString();

    mockMvc.perform(get("/schemas/" + schemaId + "/export")
        .contentType("application/x-yaml")
        .accept("application/x-yaml"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentTypeCompatibleWith("application/x-yaml"));
  }

  @Test
  public void whenInvalidSchemaId_onExportSchema_thenStatusClientError() throws Exception {
    mockMvc.perform(get("/schemas/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().is4xxClientError());
  }

  private String toJson(Object object) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper.writeValueAsString(object);
  }
}
