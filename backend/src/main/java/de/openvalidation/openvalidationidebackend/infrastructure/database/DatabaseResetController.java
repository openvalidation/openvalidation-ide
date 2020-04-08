package de.openvalidation.openvalidationidebackend.infrastructure.database;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@Hidden
public class DatabaseResetController {
  private DatabaseResetService databaseResetService;

  @Autowired
  public DatabaseResetController(DatabaseResetService databaseResetService) {
    this.databaseResetService = databaseResetService;
  }

  @DeleteMapping(value = "/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetDatabaseToInitialState() throws SQLException {
    databaseResetService.resetDatabaseToInitialState();
  }
}
