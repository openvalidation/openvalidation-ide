package de.openvalidation.openvalidationidebackend.domain.schema.attribute;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Attribute value not valid for AttributeType")
public class AttributeValueNotValidException extends RuntimeException {
}
