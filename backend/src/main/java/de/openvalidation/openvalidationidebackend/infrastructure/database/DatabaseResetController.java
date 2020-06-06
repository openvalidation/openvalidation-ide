package de.openvalidation.openvalidationidebackend.infrastructure.database;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@Hidden
public class DatabaseResetController {
  private DatabaseResetService databaseResetService;

  @Autowired
  public DatabaseResetController(DatabaseResetService databaseResetService) {
    this.databaseResetService = databaseResetService;
  }

  @DeleteMapping(value = {"/reset", "/reset/{endpointSecret}"})
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetDatabaseToInitialState(@PathVariable Optional<String> endpointSecret) {
    databaseResetService.resetDatabaseToInitialState(endpointSecret.orElse(""));
  }
}
