package de.openvalidation.openvalidationidebackend.infrastructure.database;

import de.openvalidation.openvalidationidebackend.domain.ruleset.RulesetRepository;
import de.openvalidation.openvalidationidebackend.domain.schema.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DatabaseResetService {
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;
  private DatabaseInitializer databaseInitializer;
  private String endpointSecret;

  @Autowired
  public DatabaseResetService(RulesetRepository rulesetRepository, SchemaRepository schemaRepository, DatabaseInitializer databaseInitializer,
                              @Value("${reset-secret:}") String endpointSecret) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
    this.databaseInitializer = databaseInitializer;
    this.endpointSecret = endpointSecret;
  }

  public void resetDatabaseToInitialState(String endpointSecret) {
    if (!endpointSecret.equals(this.endpointSecret)) {
      throw new InvalidResetSecret();
    }
    rulesetRepository.deleteAll();
    schemaRepository.deleteAll();
    databaseInitializer.createInitialData();
  }
}
