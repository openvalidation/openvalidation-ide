package de.openvalidation.openvalidationidebackend.infrastructure.database;

import de.openvalidation.openvalidationidebackend.domain.ruleset.RulesetRepository;
import de.openvalidation.openvalidationidebackend.domain.schema.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseResetService {
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;
  private DatabaseInitializer databaseInitializer;

  @Autowired
  public DatabaseResetService(RulesetRepository rulesetRepository, SchemaRepository schemaRepository, DatabaseInitializer databaseInitializer) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
    this.databaseInitializer = databaseInitializer;
  }

  public void resetDatabaseToInitialState() {
    rulesetRepository.deleteAll();
    schemaRepository.deleteAll();
    databaseInitializer.createInitialData();
  }
}
