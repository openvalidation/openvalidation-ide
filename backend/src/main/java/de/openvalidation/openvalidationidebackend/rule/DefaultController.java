package de.openvalidation.openvalidationidebackend.rule;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

  @GetMapping("/")
  public String getDefaultMessage() {
    return "Backend works!";
  }
}
