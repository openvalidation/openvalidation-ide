package de.openvalidation.openvalidationidebackend.infrastructure.database;

import de.openvalidation.openvalidationidebackend.domain.ruleset.Ruleset;
import de.openvalidation.openvalidationidebackend.domain.ruleset.RulesetRepository;
import de.openvalidation.openvalidationidebackend.domain.schema.Schema;
import de.openvalidation.openvalidationidebackend.domain.schema.SchemaRepository;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.Attribute;
import de.openvalidation.openvalidationidebackend.domain.schema.attribute.AttributeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class DatabaseInitializer implements ApplicationListener<ApplicationReadyEvent> {
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;

  @Autowired
  public DatabaseInitializer(RulesetRepository rulesetRepository, SchemaRepository schemaRepository) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
  }

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {
    createInitialData();
  }

  public void createInitialData() {
    if (rulesetRepository.count() == 0 && schemaRepository.count() == 0) {
      Set<Attribute> attributes;
      Set<Attribute> attributeChildren;
      Schema schema;
      Ruleset ruleset;

      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Satoshi", null));
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "25", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "hello openValidation", "Basic example", new Date(),
          "openVALIDATION IDE Team", new Date(), "your name HAS to be Validaria", schema);
      rulesetRepository.save(ruleset);

      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Satoshi", null));
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "25", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "if/then rule", "Simple condition example", new Date(),
          "openVALIDATION IDE Team", new Date(), "  IF the name IS NOT Validaria\n"
          + "THEN the Name should be Validaria", schema);
      rulesetRepository.save(ruleset);

      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Satoshi", null));
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "25", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "variables", "Introduction of variables", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "Berlin AS capital city\n"
              + "\n"
              + "the location HAS to be capital city", schema);
      rulesetRepository.save(ruleset);

      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Satoshi", null));
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "25", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "precondition", "Advanced example including variables and conditions", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "     the age is SMALLER than 18 \n"
              + "  AS underage\n"
              + "\n"
              + "     the user MUST NOT be underage\n"
              + " AND his name SHOULD be Validaria\n"
              + "\n"
              + "\n"
              + "  IF user IS NOT underage\n"
              + " AND his location IS NOT Dortmund\n"
              + "THEN sorry, your location should be Dortmund\n", schema);
      rulesetRepository.save(ruleset);

      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "20", null));
      attributes.add(new Attribute(UUID.randomUUID(), "experience", AttributeType.NUMBER, "3", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "arithmetic", "Usage of arithmetic operands", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "   user's age - 18 years\n"
              + "AS actual work experience\n"
              + "\n"
              + "the indicated professional experience MUST NOT be LARGER \n"
              + "then the actual work experience", schema);
      rulesetRepository.save(ruleset);

      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Validaria", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "nested conditions", "A condition in a condition", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "  IF user's name IS Validaria\n"
              + " AND his location IS Dortmund\n"
              + "     OR his location IS Berlin\n"
              + "THEN if the user is named Validaria, \n"
              + "     he or she must not be from Dortmund or Berlin", schema);
      rulesetRepository.save(ruleset);

/*      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "name", AttributeType.TEXT, "Satoshi", null));
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "25", null));
      attributes.add(new Attribute(UUID.randomUUID(), "location", AttributeType.TEXT, "Dortmund", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "lists", "Check if list contains attribute value", new Date(),
          "openVALIDATION IDE Team", new Date(), "a location MUST be London, Berlin or Paris", schema);
      rulesetRepository.save(ruleset); */

/*      attributes = new HashSet<>();
      attributeChildren = new HashSet<>();
      attributeChildren.add(new Attribute(UUID.randomUUID(), "z_bfm32", AttributeType.TEXT, "MPA123", null));
      attributes.add(new Attribute(UUID.randomUUID(), "MFG", AttributeType.OBJECT, null, attributeChildren));
      attributes.add(new Attribute(UUID.randomUUID(), "oZ2", AttributeType.TEXT, "Berlin", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "legacy schema", "Build more advanced rules with objects", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "     MFG.z_bfm32 IS MPA \n"
          + "  AS Student\n"
          + "\n"
          + "     oZ2\n"
          + "  AS residence\n"
          + "\n"
          + "     user SHOULD be a Student\n"
          + " AND his residence SHOULD be Dortmund", schema);
      rulesetRepository.save(ruleset); */

/*      attributes = new HashSet<>();
      attributeChildren = new HashSet<>();
      attributeChildren.add(new Attribute(UUID.randomUUID(), "1", AttributeType.TEXT, "Hans", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "2", AttributeType.TEXT, "Peter", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "3", AttributeType.TEXT, "Klaus", null));
      attributes.add(new Attribute(UUID.randomUUID(), "names", AttributeType.LIST, null, attributeChildren));
      attributes.add(new Attribute(UUID.randomUUID(), "I", AttributeType.TEXT, "Peter", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "first from list", "Reading from lists", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "first item from names as Boss\n"
          + "\n"
          + "I have to be the Boss", schema);
      rulesetRepository.save(ruleset); */

/*      attributes = new HashSet<>();
      attributeChildren = new HashSet<>();
      attributeChildren.add(new Attribute(UUID.randomUUID(), "1", AttributeType.NUMBER, "1", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "2", AttributeType.NUMBER, "2", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "3", AttributeType.NUMBER, "3", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "4", AttributeType.NUMBER, "4", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "5", AttributeType.NUMBER, "5", null));
      attributeChildren.add(new Attribute(UUID.randomUUID(), "6", AttributeType.NUMBER, "6", null));
      attributes.add(new Attribute(UUID.randomUUID(), "numbers", AttributeType.LIST, null, attributeChildren));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "first from list with condition", "Advanced reading from lists", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "first number from numbers with a value bigger than 3 as magic number\n"
          + "\n"
          + "magic number has to be 4", schema);
      rulesetRepository.save(ruleset); */

/*      attributes = new HashSet<>();
      attributes.add(new Attribute(UUID.randomUUID(), "age", AttributeType.NUMBER, "17", null));
      schema = new Schema(UUID.randomUUID(), attributes);
      schemaRepository.save(schema);
      ruleset = new Ruleset(UUID.randomUUID(), "semantic operators", "Usage of semantic operators", new Date(),
          "openVALIDATION IDE Team", new Date(),
          "age and smaller\n"
          + "as operator younger\n"
          + "\n"
          + "user must not be younger than 18 years", schema);
      rulesetRepository.save(ruleset); */
    }
  }
}
