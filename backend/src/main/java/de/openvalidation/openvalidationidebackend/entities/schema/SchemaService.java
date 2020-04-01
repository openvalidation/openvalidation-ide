package de.openvalidation.openvalidationidebackend.entities.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import de.openvalidation.openvalidationidebackend.core.DtoMapper;
import de.openvalidation.openvalidationidebackend.entities.attribute.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchemaService {
  private final DtoMapper dtoMapper = Mappers.getMapper(DtoMapper.class);
  private SchemaRepository schemaRepository;

  @Autowired
  public SchemaService(SchemaRepository schemaRepository) {
    this.schemaRepository = schemaRepository;
  }

  public List<SchemaDto> getAllSchemas() {
    return dtoMapper.toSchemaDtoList(schemaRepository.findAll());
  }

  public SchemaDto getSchema(UUID schemaId) {
    return dtoMapper.toSchemaDto(schemaRepository.findById(schemaId)
        .orElseThrow(SchemaNotFoundException::new));
  }

  public SchemaDto updateSchema(UUID schemaId, SchemaUpdateDto schemaUpdateDto) {
    return dtoMapper.toSchemaDto(
        schemaRepository.save(schemaRepository.findById(schemaId)
            .orElseThrow(SchemaNotFoundException::new)
            .updateBySchema(dtoMapper.toSchemaEntity(schemaUpdateDto))));
  }

  public Set<AttributeDto> getAllAttributesFromSchema(UUID schemaId) {
    return dtoMapper.toAttributeDtoSet(schemaRepository.findById(schemaId)
        .orElseThrow(SchemaNotFoundException::new).getAttributes());
  }

  public Set<AttributeDto> addAttributesToSchema(UUID schemaId, Set<AttributeCreateDto> attributeCreateDtos) {
    Set<Attribute> attributesCreated = new HashSet<>();
    attributeCreateDtos.forEach(attributeCreateDto ->
        attributesCreated.add(dtoMapper.toAttributeEntity(attributeCreateDto)));
    return dtoMapper.toAttributeDtoSet(schemaRepository.save(schemaRepository.findById(schemaId).map(schema -> {
      Set<Attribute> attributes = schema.getAttributes();
      attributes.addAll(attributesCreated);
      schema.setAttributes(attributes);
      return schema;
    }).orElseThrow(SchemaNotFoundException::new)).getAttributes().parallelStream()
        .filter(attribute -> {
          for (Attribute attributeCreated : attributesCreated) {
            if (attributeCreated.getAttributeId().equals(attribute.getAttributeId())) {
              return true;
            }
          }
          return false;
        }).collect(Collectors.toSet()));
  }

  public AttributeDto getAttributeFromSchema(UUID schemaId, UUID attributeId) {
    return dtoMapper.toAttributeDto(schemaRepository.findById(schemaId)
        .orElseThrow(SchemaNotFoundException::new).getAttributes().parallelStream()
        .filter(attribute -> attribute.getAttributeId().equals(attributeId)).findFirst()
        .orElseThrow(AttributeNotFoundException::new));
  }

  public AttributeDto updateAttributeFromSchema(UUID schemaId, UUID attributeId, AttributeUpdateDto attributeUpdateDto) {
    return dtoMapper.toAttributeDto(
        schemaRepository.save(schemaRepository.findById(schemaId).map(schema -> {
          Set<Attribute> attributes = schema.getAttributes();
          Attribute updatedAttribute = attributes.parallelStream().filter(attribute -> attribute.getAttributeId().equals(attributeId))
              .findFirst().orElseThrow(AttributeNotFoundException::new);
          attributes.removeIf(attribute -> attribute.getAttributeId().equals(attributeId));
          attributes.add(updatedAttribute.updateByAttribute(dtoMapper.toAttributeEntity(attributeUpdateDto)));
          schema.setAttributes(attributes);
          return schema;
        }).orElseThrow(SchemaNotFoundException::new)).getAttributes().parallelStream()
            .filter(attribute -> attribute.getAttributeId().equals(attributeId)).findFirst()
            .orElseThrow(AttributeNotFoundException::new));
  }

  public void deleteAttributeFromSchema(UUID schemaId, UUID attributeId) {
    schemaRepository.save(schemaRepository.findById(schemaId).map(schema -> {
      Set<Attribute> attributes = schema.getAttributes();
      attributes.removeIf(attribute -> attribute.getAttributeId().equals(attributeId));
      schema.setAttributes(attributes);
      return schema;
    }).orElseThrow(SchemaNotFoundException::new));
  }

  public String exportSchema(UUID schemaId, String mediaType) throws JsonProcessingException {
    Schema schema = schemaRepository.findById(schemaId).orElseThrow(SchemaNotFoundException::new);
    ObjectMapper objectMapper = getSpecificObjectMapper(mediaType);

    return objectMapper.writeValueAsString(convertAttributesToMap(schema.getAttributes()));
  }

  private ObjectMapper getSpecificObjectMapper(String mediaType) {
    if (mediaType.equalsIgnoreCase("application/x-yaml")) {
      return new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
    }
    return new ObjectMapper();
  }

  private Map<String, Object> convertAttributesToMap(Set<Attribute> attributes) {
    Map<String, Object> map = new HashMap<>();
    attributes.forEach(attribute -> {
      switch (attribute.getAttributeType()) {
        case TEXT:
          map.put(attribute.getName(), attribute.getValue());
          break;
        case NUMBER:
          map.put(attribute.getName(), Double.parseDouble(attribute.getValue()));
          break;
        case BOOLEAN:
          map.put(attribute.getName(), Boolean.parseBoolean(attribute.getValue()));
          break;
        case LIST:
          map.put(attribute.getName(), convertAttributesToList(attribute.getChildren()));
          break;
        case OBJECT:
          map.put(attribute.getName(), convertAttributesToMap(attribute.getChildren()));
          break;
      }
    });
    return map;
  }

  private List<Object> convertAttributesToList(Set<Attribute> attributes) {
    List<Object> list = new ArrayList<>();
    attributes.forEach(attribute -> {
      switch (attribute.getAttributeType()) {
        case TEXT:
          list.add(attribute.getValue());
          break;
        case NUMBER:
          list.add(Double.parseDouble(attribute.getValue()));
          break;
        case BOOLEAN:
          list.add(Boolean.parseBoolean(attribute.getValue()));
          break;
        case LIST:
          list.add(convertAttributesToList(attribute.getChildren()));
          break;
        case OBJECT:
          list.add(convertAttributesToMap(attribute.getChildren()));
          break;
      }
    });
    return list;
  }
}
