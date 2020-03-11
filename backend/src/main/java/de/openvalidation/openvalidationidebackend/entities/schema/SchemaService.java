package de.openvalidation.openvalidationidebackend.entities.schema;

import de.openvalidation.openvalidationidebackend.core.DtoMapper;
import de.openvalidation.openvalidationidebackend.entities.attribute.*;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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

  public Set<AttributeDto> createAttributesFromSchema(UUID schemaId, Set<AttributeCreateDto> attributeCreateDtos) {
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
}
