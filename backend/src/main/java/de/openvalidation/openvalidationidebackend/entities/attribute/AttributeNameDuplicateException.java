package de.openvalidation.openvalidationidebackend.entities.attribute;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Attribute name must be unique in schema")
public class AttributeNameDuplicateException extends RuntimeException {
}
