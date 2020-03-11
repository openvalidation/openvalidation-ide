package de.openvalidation.openvalidationidebackend.entities.attribute;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Attribute not found")
public class AttributeNotFoundException extends RuntimeException {
}
