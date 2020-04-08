package de.openvalidation.openvalidationidebackend.domain.ruleset;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.openvalidation.openvalidationidebackend.domain.schema.SchemaRepository;
import org.apache.commons.lang3.RandomStringUtils;
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
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RulesetIntegrationTest {
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

    Ruleset ruleset = new RulesetBuilder().build();
    schemaRepository.save(ruleset.getSchema());
    rulesetRepository.save(ruleset);
  }

  @AfterEach
  public void clearRepository() {
    rulesetRepository.deleteAll();
    schemaRepository.deleteAll();
  }

  @Test
  public void onGetAll_thenStatusSuccessful() throws Exception {
    String rulesetId = new RulesetBuilder().build().getRulesetId().toString();

    mockMvc.perform(get("/rulesets")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].rulesetId", is(rulesetId)));
  }

  @Test
  public void whenValidRequestBody_onCreateRuleset_thenStatusSuccessful() throws Exception {
    RulesetCreateDto rulesetCreateDto = new RulesetBuilder().buildCreateDto();

    mockMvc.perform(post("/rulesets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(rulesetCreateDto)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void whenEmptyRequestBody_onCreateRuleset_thenStatusSuccessful() throws Exception {
    mockMvc.perform(post("/rulesets")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("Unnamed ruleset")));
  }

  @Test
  public void whenInvalidRequestBody_onCreateRuleset_thenStatusClientError() throws Exception {
    RulesetCreateDto rulesetCreateDto = new RulesetBuilder().setName(RandomStringUtils.randomAlphabetic(300)).buildCreateDto();

    mockMvc.perform(post("/rulesets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(rulesetCreateDto)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidId_onGet_thenStatusSuccessful() throws Exception {
    String rulesetId = new RulesetBuilder().build().getRulesetId().toString();

    mockMvc.perform(get("/rulesets/" + rulesetId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.rulesetId", is(rulesetId)));
  }

  @Test
  public void whenInvalidId_onGet_thenStatusClientError() throws Exception {
    mockMvc.perform(get("/rulesets/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidId_onUpdate_thenStatusSuccessful() throws Exception {
    Ruleset updateRuleset = new RulesetBuilder().build();

    mockMvc.perform(put("/rulesets/" + updateRuleset.getRulesetId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(updateRuleset)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.rulesetId", is(updateRuleset.getRulesetId().toString())));
  }

  @Test
  public void whenInvalidId_onUpdate_thenStatusClientError() throws Exception {
    Ruleset updateRuleset = new RulesetBuilder().build();

    mockMvc.perform(put("/rulesets/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(toJson(updateRuleset)))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void whenValidId_onDeleteRuleset_thenStatusSuccessful() throws Exception {
    String rulesetId = new RulesetBuilder().build().getRulesetId().toString();

    mockMvc.perform(delete("/rulesets/" + rulesetId)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  public void whenInvalidId_onDeleteRuleset_thenStatusServerError() throws Exception {
    mockMvc.perform(delete("/rulesets/" + UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError());
  }

  private String toJson(Object object) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return objectMapper.writeValueAsString(object);
  }

}
