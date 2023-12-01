package com.spotit.backend.foreignLanguage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;

public interface ForeignLanguageNameRepository extends JpaRepository<ForeignLanguageName, Integer> {

    Optional<ForeignLanguageName> findByName(String name);
}
