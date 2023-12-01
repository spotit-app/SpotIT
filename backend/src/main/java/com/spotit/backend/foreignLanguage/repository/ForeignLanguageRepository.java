package com.spotit.backend.foreignLanguage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotit.backend.foreignLanguage.model.ForeignLanguage;

public interface ForeignLanguageRepository extends JpaRepository<ForeignLanguage, Integer> {

    List<ForeignLanguage> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
