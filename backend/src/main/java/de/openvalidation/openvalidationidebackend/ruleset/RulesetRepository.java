package de.openvalidation.openvalidationidebackend.ruleset;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RulesetRepository extends JpaRepository<Ruleset, UUID> {
}
