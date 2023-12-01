package com.spotit.backend.foreignLanguage.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spotit.backend.abstraction.controller.AbstractUserDetailController;
import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageDto;
import com.spotit.backend.foreignLanguage.dto.WriteForeignLanguageDto;
import com.spotit.backend.foreignLanguage.mapper.ForeignLanguageMapper;
import com.spotit.backend.foreignLanguage.model.ForeignLanguage;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;
import com.spotit.backend.foreignLanguage.service.ForeignLanguageNameService;
import com.spotit.backend.foreignLanguage.service.ForeignLanguageService;

@RestController
@RequestMapping("/api/userAccount/{auth0Id}/foreignLanguage")
public class ForeignLanguageController extends
        AbstractUserDetailController<ForeignLanguage, Integer, ReadForeignLanguageDto, WriteForeignLanguageDto> {

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
    public ReadForeignLanguageDto createEntityOfUser(
            @PathVariable String auth0Id,
            @RequestBody WriteForeignLanguageDto writeDto) {
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
