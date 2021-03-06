package de.openvalidation.openvalidationidebackend.domain.schema;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Schema not found")
public class SchemaNotFoundException extends RuntimeException {
}
