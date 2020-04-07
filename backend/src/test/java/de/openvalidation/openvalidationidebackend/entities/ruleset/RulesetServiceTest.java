package de.openvalidation.openvalidationidebackend.entities.ruleset;

import de.openvalidation.openvalidationidebackend.entities.schema.Schema;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaRepository;
import de.openvalidation.openvalidationidebackend.util.RulesetBuilder;
import de.openvalidation.openvalidationidebackend.util.SchemaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RulesetDtoMapperImpl.class)
public class RulesetServiceTest {
  @MockBean
  private RulesetRepository rulesetRepository;
  @MockBean
  private SchemaRepository schemaRepository;
  @Autowired
  private RulesetDtoMapper rulesetDtoMapper;

  private RulesetService rulesetService;
  private Ruleset ruleset;
  private UUID validRulesetId = UUID.randomUUID();
  private UUID validSchemaId = UUID.randomUUID();

  @BeforeEach
  public void setUp() {
    rulesetService = new RulesetService(rulesetRepository, schemaRepository, rulesetDtoMapper);
    ruleset = new RulesetBuilder().setRulesetId(validRulesetId).build();

    List<Ruleset> rulesets = new ArrayList<>();
    rulesets.add(ruleset);
    rulesets.add(ruleset);
    rulesets.add(ruleset);

    when(rulesetRepository.findAll()).thenReturn(rulesets);
    when(rulesetRepository.findById(validRulesetId)).thenReturn(Optional.of(ruleset));
    when(rulesetRepository.save(any(Ruleset.class))).thenAnswer(invocation -> invocation.getArgument(0));

    when(schemaRepository.findById(validSchemaId))
        .thenReturn(Optional.of(new SchemaBuilder().setSchemaId(validSchemaId).build()));
    when(schemaRepository.save(any(Schema.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  public void whenGetAllRulesets_thenReturnAllRulesets() {
    assertThat(rulesetService.getAllRulesets()).isInstanceOf(List.class);
    assertThat(rulesetService.getAllRulesets()).hasSize(3);
  }

  @Test
  public void whenCreateRulesetAndSchmeaIsGiven_thenReturnRulesetWithLinkedSchema() {
    RulesetCreateDto rulesetCreateDto = new RulesetBuilder()
        .setSchema(new SchemaBuilder().setSchemaId(validSchemaId).build())
        .buildCreateDto();
    RulesetDto rulesetDto = rulesetService.createRuleset(rulesetCreateDto);
    assertThat(rulesetDto.getSchemaId()).isEqualByComparingTo(validSchemaId);
  }

  @Test
  public void whenCreateRulesetAndSchmeaIsNotGiven_thenReturnRulesetWithNewSchema() {
    RulesetCreateDto rulesetCreateDto = new RulesetBuilder().setSchema(null).buildCreateDto();
    RulesetDto rulesetDto = rulesetService.createRuleset(rulesetCreateDto);
    assertThat(rulesetDto.getSchemaId()).isNotEqualByComparingTo(validSchemaId);
  }

  @Test
  public void whenValidRulesetId_onGetRuleset_thenRulesetShouldBeFound() {
    RulesetDto rulesetDto = rulesetService.getRuleset(ruleset.getRulesetId());
    assertThat(rulesetDto.getRulesetId()).isEqualByComparingTo(validRulesetId);
  }

  @Test
  public void whenInvalidRulesetId_onGetRuleset_thenExceptionShouldBeThrown() {
    assertThrows(RulesetNotFoundException.class, () -> rulesetService.getRuleset(UUID.randomUUID()));
  }

  @Test
  public void whenValidRulesetId_onUpdateRuleset_thenRulesetShouldBeUpdated() {
    RulesetUpdateDto rulesetUpdateDto = new RulesetBuilder().buildUpdateDto();
    RulesetDto rulesetDto = rulesetService.updateRuleset(validRulesetId, rulesetUpdateDto);
    assertThat(rulesetDto.getRulesetId()).isEqualByComparingTo(validRulesetId);
  }

  @Test
  public void whenInvalidRulesetId_onUpdateRuleset_thenExceptionShouldBeThrown() {
    assertThrows(RulesetNotFoundException.class,
        () -> rulesetService.updateRuleset(UUID.randomUUID(), new RulesetBuilder().buildUpdateDto()));
  }

  @Test
  public void whenValidRulesetId_onDeleteRuleset_thenNoExceptionShouldBeThrown() {
    assertDoesNotThrow(() -> rulesetService.deleteRuleset(validRulesetId));
  }

  @Test
  public void whenInvalidRulesetId_onDeleteRuleset_thenExceptionShouldBeThrown() {
    assertThrows(RulesetNotFoundException.class, () -> rulesetService.deleteRuleset(UUID.randomUUID()));
  }
}