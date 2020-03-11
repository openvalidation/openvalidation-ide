package de.openvalidation.openvalidationidebackend.entities.schema;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchemaRepository extends JpaRepository<Schema, UUID> {
}
