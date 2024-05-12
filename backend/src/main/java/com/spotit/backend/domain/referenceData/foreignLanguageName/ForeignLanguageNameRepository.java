package com.spotit.backend.domain.referenceData.foreignLanguageName;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ForeignLanguageNameRepository extends JpaRepository<ForeignLanguageName, Integer> {

    Optional<ForeignLanguageName> findByName(String name);
}
