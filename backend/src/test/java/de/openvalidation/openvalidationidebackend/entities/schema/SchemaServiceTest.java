package de.openvalidation.openvalidationidebackend.entities.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import de.openvalidation.openvalidationidebackend.core.DtoMapper;
import de.openvalidation.openvalidationidebackend.entities.attribute.*;
import de.openvalidation.openvalidationidebackend.util.AttributeBuilder;
import de.openvalidation.openvalidationidebackend.util.SchemaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SchemaServiceTest {
  @MockBean
  private SchemaRepository schemaRepository;
  @Spy
  private DtoMapper dtoMapper = Mappers.getMapper(DtoMapper.class);

  private SchemaService schemaService;
  private UUID validSchemaId = UUID.randomUUID();
  private UUID invalidSchemaId = UUID.randomUUID();

  @BeforeEach
  public void setUp() {
    schemaService = new SchemaService(schemaRepository, dtoMapper);

    List<Schema> schemas = new ArrayList<>();
    schemas.add(new SchemaBuilder().build());
    schemas.add(new SchemaBuilder().build());

    Schema testSchema = new SchemaBuilder().setSchemaId(validSchemaId).build();

    when(schemaRepository.findAll()).thenReturn(schemas);
    when(schemaRepository.findById(validSchemaId)).thenReturn(Optional.of(testSchema));
    when(schemaRepository.findById(invalidSchemaId)).thenReturn(Optional.empty());
    when(schemaRepository.save(any(Schema.class))).thenAnswer(invocation -> invocation.getArgument(0));
  }

  @Test
  public void onGetAll_thenAllSchemasShouldBeFound() {
    List<SchemaDto> foundSchemas = schemaService.getAllSchemas();
    assertThat(foundSchemas).hasSize(2);
  }

  @Test
  public void whenValidId_onGet_thenSchemaShouldBeFound() {
    SchemaDto foundSchema = schemaService.getSchema(validSchemaId);
    assertThat(foundSchema.getSchemaId()).isEqualByComparingTo(validSchemaId);
  }

  @Test
  public void whenInvalidId_onGet_thenExceptionShouldBeThrown() {
    assertThrows(SchemaNotFoundException.class, () -> schemaService.getSchema(invalidSchemaId));
  }

  @Test
  public void whenValidId_onUpdate_thenSchemaShouldBeReturned() {
    SchemaUpdateDto schemaUpdateDto = createSchemaUpdateDtoFromSchemaEntity(new SchemaBuilder().build());

    SchemaDto updatedSchema = schemaService.updateSchema(validSchemaId, schemaUpdateDto);
    assertThat(updatedSchema.getSchemaId()).isEqualByComparingTo(validSchemaId);
    assertThat(updatedSchema.getAttributes()).hasSameSizeAs(new SchemaBuilder().build().getAttributes());
  }

  @Test
  public void whenInvalidId_onUpdate_thenExceptionShouldBeThrown() {
    SchemaUpdateDto schemaUpdateDto = createSchemaUpdateDtoFromSchemaEntity(new SchemaBuilder().build());

    assertThrows(SchemaNotFoundException.class, () -> schemaService.updateSchema(invalidSchemaId, schemaUpdateDto));
  }

  @Test
  public void whenValidId_onGetAllAttributes_thenAllAttributesShouldBeFound() {
    Set<AttributeDto> foundAttributes = schemaService.getAllAttributesFromSchema(validSchemaId);
    assertThat(foundAttributes).hasSameSizeAs(new SchemaBuilder().build().getAttributes());
  }

  @Test
  public void whenInvalidId_onGetAllAttributes_thenExceptionShouldBeThrown() {
    assertThrows(SchemaNotFoundException.class, () -> schemaService.getAllAttributesFromSchema(invalidSchemaId));
  }

  @Test
  public void whenValidId_onCreateAttributes_thenNewAttributesShouldBeReturned() {
    Set<AttributeCreateDto> attributeCreateDtos = new HashSet<>();
    attributeCreateDtos.add(createAttributeCreateDtoFromAttributeEntity(new AttributeBuilder().build()));
    attributeCreateDtos.add(createAttributeCreateDtoFromAttributeEntity(new AttributeBuilder().build()));

    Set<AttributeDto> createdAttributes = schemaService.addAttributesToSchema(validSchemaId, attributeCreateDtos);
    assertThat(createdAttributes).hasSameSizeAs(attributeCreateDtos);
  }

  @Test
  public void whenInvalidId_onCreateAttributes_thenExceptionShouldBeThrown() {
    Set<AttributeCreateDto> attributeCreateDtos = new HashSet<>();
    attributeCreateDtos.add(createAttributeCreateDtoFromAttributeEntity(new AttributeBuilder().build()));

    assertThrows(SchemaNotFoundException.class, () -> schemaService.addAttributesToSchema(invalidSchemaId, attributeCreateDtos));
  }

  @Test
  public void whenDuplicateAttributeName_onCreateAttributes_thenExceptionShouldBeThrown() {
    Set<AttributeCreateDto> attributeCreateDtos = new HashSet<>();
    attributeCreateDtos.add(createAttributeCreateDtoFromAttributeEntity(new AttributeBuilder().setName("dup").build()));
    attributeCreateDtos.add(createAttributeCreateDtoFromAttributeEntity(new AttributeBuilder().setName("dup").build()));

    assertThrows(AttributeNameDuplicateException.class, () -> schemaService.addAttributesToSchema(validSchemaId, attributeCreateDtos));
  }

  @Test
  public void whenInvalidValue_onCreateAttributes_thenExceptionShouldBeThrown() {
    Set<AttributeCreateDto> attributeCreateDtos = new HashSet<>();
    attributeCreateDtos.add(createAttributeCreateDtoFromAttributeEntity(
        new AttributeBuilder().setAttributeType(AttributeType.NUMBER).setValue("Test_NaN").build()));

    assertThrows(AttributeValueNotValidException.class, () -> schemaService.addAttributesToSchema(validSchemaId, attributeCreateDtos));
  }

  @Test
  public void whenValidIds_onGetAttribute_thenAttributeShouldBeFound() {
    UUID attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId();
    AttributeDto foundAttribute = schemaService.getAttributeFromSchema(validSchemaId, attributeId);
    assertThat(foundAttribute.getAttributeId()).isEqualByComparingTo(attributeId);
  }

  @Test
  public void whenInvalidSchemaId_onGetAttribute_thenExceptionShouldBeThrown() {
    UUID attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId();

    assertThrows(SchemaNotFoundException.class, () -> schemaService.getAttributeFromSchema(invalidSchemaId, attributeId));
  }

  @Test
  public void whenInvalidAttributeId_onGetAttribute_thenExceptionShouldBeThrown() {
    assertThrows(AttributeNotFoundException.class, () -> schemaService.getAttributeFromSchema(validSchemaId, UUID.randomUUID()));
  }

  @Test
  public void whenValidIds_onUpdateAttribute_thenAttributeShouldBeReturned() {
    UUID attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId();
    AttributeUpdateDto attributeUpdateDto = createAttributeUpdateDtoFromAttributeEntity(new AttributeBuilder().build());

    AttributeDto updatedAttribute = schemaService.updateAttributeFromSchema(validSchemaId, attributeId, attributeUpdateDto);
    assertThat(updatedAttribute.getAttributeId()).isEqualByComparingTo(attributeId);
  }

  @Test
  public void whenInvalidSchemaId_onUpdateAttribute_thenExceptionShouldBeThrown() {
    UUID attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId();
    AttributeUpdateDto attributeUpdateDto = createAttributeUpdateDtoFromAttributeEntity(new AttributeBuilder().build());

    assertThrows(SchemaNotFoundException.class, () -> schemaService.updateAttributeFromSchema(invalidSchemaId, attributeId, attributeUpdateDto));
  }

  @Test
  public void whenInvalidAttributeId_onUpdateAttribute_thenExceptionShouldBeThrown() {
    AttributeUpdateDto attributeUpdateDto = createAttributeUpdateDtoFromAttributeEntity(new AttributeBuilder().build());

    assertThrows(AttributeNotFoundException.class, () -> schemaService.updateAttributeFromSchema(validSchemaId, UUID.randomUUID(), attributeUpdateDto));
  }

  @Test
  public void whenValidIds_onDeleteAttribute_thenNoExceptionShouldBeThrown() {
    UUID attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId();

    assertDoesNotThrow(() -> schemaService.deleteAttributeFromSchema(validSchemaId, attributeId));
  }

  @Test
  public void whenInvalidSchemaId_onDeleteAttribute_thenExceptionShouldBeThrown() {
    UUID attributeId = new SchemaBuilder().build().getAttributes().iterator().next().getAttributeId();

    assertThrows(SchemaNotFoundException.class, () -> schemaService.deleteAttributeFromSchema(invalidSchemaId, attributeId));
  }

  @Test
  public void whenInvalidAttributeId_onDeleteAttribute_thenNoExceptionShouldBeThrown() {
    assertDoesNotThrow(() -> schemaService.deleteAttributeFromSchema(validSchemaId, UUID.randomUUID()));
  }

  @Test
  public void whenValidSchemaId_onExportSchemaJson_thenJsonShouldBeReturned() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    String exported = schemaService.exportSchema(validSchemaId, MediaType.APPLICATION_JSON_VALUE);
    Map<String, Object> map = objectMapper.readValue(exported, new TypeReference<Map<String, Object>>() {
    });
    for (AttributeDto attributeDto : schemaService.getSchema(validSchemaId).getAttributes()) {
      assertThat(map.get(attributeDto.getName()).toString().equals(attributeDto.getValue()));
    }
  }

  @Test
  public void whenValidSchemaId_onExportSchemaYaml_thenYamlShouldBeReturned() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    String exported = schemaService.exportSchema(validSchemaId, "application/x-yaml");
    Map<String, Object> map = objectMapper.readValue(exported, new TypeReference<Map<String, Object>>() {
    });
    for (AttributeDto attributeDto : schemaService.getSchema(validSchemaId).getAttributes()) {
      assertThat(map.get(attributeDto.getName()).toString().equals(attributeDto.getValue()));
    }
  }

  @Test
  public void whenInvalidSchemaId_onExportSchema_thenExceptionShouldBeThrown() {
    assertThrows(SchemaNotFoundException.class, () -> schemaService.exportSchema(invalidSchemaId, MediaType.APPLICATION_JSON_VALUE));
  }

  private SchemaUpdateDto createSchemaUpdateDtoFromSchemaEntity(Schema schema) {
    SchemaUpdateDto schemaUpdateDto = new SchemaUpdateDto();
    Set<AttributeUpdateDto> attributes = new HashSet<>();
    for (Attribute attribute : schema.getAttributes()) {
      attributes.add(createAttributeUpdateDtoFromAttributeEntity(attribute));
    }
    schemaUpdateDto.setAttributes(attributes);
    return schemaUpdateDto;
  }

  private AttributeUpdateDto createAttributeUpdateDtoFromAttributeEntity(Attribute attribute) {
    AttributeUpdateDto attributeUpdateDto = new AttributeUpdateDto();
    attributeUpdateDto.setName(attribute.getName());
    attributeUpdateDto.setAttributeType(attribute.getAttributeType());
    attributeUpdateDto.setValue(attribute.getValue());
    Set<AttributeUpdateDto> children = new HashSet<>();
    for (Attribute child : attribute.getChildren()) {
      children.add(createAttributeUpdateDtoFromAttributeEntity(child));
    }
    attributeUpdateDto.setChildren(children);
    return attributeUpdateDto;
  }

  private AttributeCreateDto createAttributeCreateDtoFromAttributeEntity(Attribute attribute) {
    AttributeCreateDto attributeCreateDto = new AttributeCreateDto();
    attributeCreateDto.setName(attribute.getName());
    attributeCreateDto.setAttributeType(attribute.getAttributeType());
    attributeCreateDto.setValue(attribute.getValue());
    Set<AttributeCreateDto> children = new HashSet<>();
    for (Attribute child : attribute.getChildren()) {
      children.add(createAttributeCreateDtoFromAttributeEntity(child));
    }
    attributeCreateDto.setChildren(children);
    return attributeCreateDto;
  }
}
