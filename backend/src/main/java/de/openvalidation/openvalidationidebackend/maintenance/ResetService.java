package de.openvalidation.openvalidationidebackend.maintenance;

import de.openvalidation.openvalidationidebackend.entities.ruleset.RulesetRepository;
import de.openvalidation.openvalidationidebackend.entities.schema.SchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


@Service
public class ResetService {
  private RulesetRepository rulesetRepository;
  private SchemaRepository schemaRepository;
  private DataSource dataSource;

  @Autowired
  public ResetService(RulesetRepository rulesetRepository, SchemaRepository schemaRepository, DataSource dataSource) {
    this.rulesetRepository = rulesetRepository;
    this.schemaRepository = schemaRepository;
    this.dataSource = dataSource;
  }

  public void resetDatabaseToInitialState() throws SQLException {
    rulesetRepository.deleteAll();
    schemaRepository.deleteAll();

    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();

    try (Connection connection = dataSource.getConnection()) {
      if (connection.getMetaData().getDatabaseProductName().equalsIgnoreCase("h2")) {
        resourceDatabasePopulator.addScript(new ClassPathResource("data-h2.sql"));
      } else {
        resourceDatabasePopulator.addScript(new ClassPathResource("data-postgresql.sql"));
      }

      DatabasePopulatorUtils.execute(resourceDatabasePopulator, dataSource);
    }
  }
}
