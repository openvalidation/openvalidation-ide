package de.openvalidation.openvalidationidebackend.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class ResetController {
  private ResetService resetService;

  @Autowired
  public ResetController(ResetService resetService) {
    this.resetService = resetService;
  }

  @GetMapping(value = "/reset")
  public void resetDatabaseToInitialState() {
    resetService.resetDatabaseToInitialState();
  }
}
