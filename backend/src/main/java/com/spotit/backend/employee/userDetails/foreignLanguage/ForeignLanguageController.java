package com.spotit.backend.employee.userDetails.foreignLanguage;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.employee.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.employee.referenceData.foreignLanguageName.ForeignLanguageNameService;
import com.spotit.backend.employee.userDetails.abstraction.AbstractUserDetailController;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/foreignLanguage")
public class ForeignLanguageController extends
        AbstractUserDetailController<ForeignLanguage, Integer, ForeignLanguageReadDto, ForeignLanguageWriteDto> {

    private final ForeignLanguageNameService foreignLanguageNameService;

    public ForeignLanguageController(
            ForeignLanguageService foreignLanguageService,
            ForeignLanguageNameService foreignLanguageNameService,
            ForeignLanguageMapper foreignLanguageMapper) {
        super(foreignLanguageService, foreignLanguageMapper);
        this.foreignLanguageNameService = foreignLanguageNameService;
    }

    @Override
    @PostMapping
    public ForeignLanguageReadDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody ForeignLanguageWriteDto writeDto) {

        ForeignLanguageName foreignLanguageName = foreignLanguageNameService
                .getById(writeDto.foreignLanguageNameId());

        ForeignLanguage foreignLanguageToCreate = ForeignLanguage.builder()
                .foreignLanguageName(foreignLanguageName)
                .languageLevel(writeDto.languageLevel())
                .build();

        return mapper.toReadDto(
                service.create(auth0Id, foreignLanguageToCreate));
    }
}
