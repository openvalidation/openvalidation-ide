package de.openvalidation.openvalidationidebackend.maintenance;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@CrossOrigin
@RestController
@Hidden
public class ResetController {
  private ResetService resetService;

  @Autowired
  public ResetController(ResetService resetService) {
    this.resetService = resetService;
  }

  @DeleteMapping(value = "/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetDatabaseToInitialState() throws SQLException {
    resetService.resetDatabaseToInitialState();
  }
}
