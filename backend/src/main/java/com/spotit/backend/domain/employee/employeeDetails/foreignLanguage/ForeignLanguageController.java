package com.spotit.backend.domain.employee.employeeDetails.foreignLanguage;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.domain.employee.employeeDetails.abstraction.AbstractUserDetailController;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageName;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameService;

@CrossOrigin
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
