package com.spotit.backend.foreignLanguage.controller;

import static com.spotit.backend.utils.FileUtils.getBytesFromFile;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spotit.backend.abstraction.controller.AbstractController;
import com.spotit.backend.foreignLanguage.dto.ReadForeignLanguageNameDto;
import com.spotit.backend.foreignLanguage.dto.WriteForeignLanguageNameDto;
import com.spotit.backend.foreignLanguage.mapper.ForeignLanguageNameMapper;
import com.spotit.backend.foreignLanguage.model.ForeignLanguageName;
import com.spotit.backend.foreignLanguage.service.ForeignLanguageNameService;

@RestController
@RequestMapping("/api/foreignLanguageName")
public class ForeignLanguageNameController extends
        AbstractController<ForeignLanguageName, Integer, ReadForeignLanguageNameDto, WriteForeignLanguageNameDto> {

    private final ForeignLanguageNameService foreignLanguageNameService;

    public ForeignLanguageNameController(
            ForeignLanguageNameService foreignLanguageNameService,
            ForeignLanguageNameMapper foreignLanguageNameMapper) {
        super(foreignLanguageNameService, foreignLanguageNameMapper);
        this.foreignLanguageNameService = foreignLanguageNameService;
    }

    @GetMapping
    public List<ReadForeignLanguageNameDto> getAll() {
        return foreignLanguageNameService.getAll()
                .stream()
                .map(mapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ReadForeignLanguageNameDto updateForeignLanguageNameDto(
            @PathVariable Integer id,
            @RequestPart Optional<String> name,
            @RequestPart Optional<MultipartFile> flag) {
        ForeignLanguageName updatedForeignLanguageName = foreignLanguageNameService.update(
                id,
                name.orElse(null),
                getBytesFromFile(flag.orElse(null)));

        return mapper.toReadDto(updatedForeignLanguageName);
    }

    @PostMapping
    public ReadForeignLanguageNameDto createForeignLanguageName(
            @RequestPart String name,
            @RequestPart MultipartFile flag) {
        ForeignLanguageName createdForeignLanguageName = foreignLanguageNameService.create(
                name,
                getBytesFromFile(flag));

        return mapper.toReadDto(createdForeignLanguageName);
    }
}
