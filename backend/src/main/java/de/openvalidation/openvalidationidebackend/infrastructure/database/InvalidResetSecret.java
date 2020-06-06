package de.openvalidation.openvalidationidebackend.infrastructure.database;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Invalid reset secret")
public class InvalidResetSecret extends RuntimeException {
}
