package de.openvalidation.openvalidationidebackend.rule;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RuleController {

  @GetMapping("/rules")
  public String getRule() {
    return "Backend works!";
  }
}
