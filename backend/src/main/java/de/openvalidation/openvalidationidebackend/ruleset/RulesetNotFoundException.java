package de.openvalidation.openvalidationidebackend.ruleset;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Ruleset not found")
public class RulesetNotFoundException extends RuntimeException {
}
