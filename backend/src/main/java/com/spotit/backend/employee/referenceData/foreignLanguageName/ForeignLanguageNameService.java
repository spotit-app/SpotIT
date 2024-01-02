package com.spotit.backend.employee.referenceData.foreignLanguageName;

import com.spotit.backend.employee.referenceData.abstraction.AbstractReferenceDataService;

public interface ForeignLanguageNameService extends AbstractReferenceDataService<ForeignLanguageName, Integer> {

    ForeignLanguageName create(String name, byte[] flag);

    ForeignLanguageName update(Integer id, String name, byte[] flag);
}
