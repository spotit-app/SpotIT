package com.spotit.backend.foreignLanguage.service;

import java.util.List;

import com.spotit.backend.abstraction.service.AbstractService;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;

public interface ForeignLanguageNameService extends AbstractService<ForeignLanguageName, Integer> {

    List<ForeignLanguageName> getAll();

    ForeignLanguageName create(String name, byte[] flag);

    ForeignLanguageName update(Integer id, String name, byte[] flag);
}
