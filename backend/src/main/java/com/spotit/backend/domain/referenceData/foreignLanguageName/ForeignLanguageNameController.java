package com.spotit.backend.domain.referenceData.foreignLanguageName;

import static com.spotit.backend.utils.FileUtils.getBytesFromFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/api/foreignLanguageName")
public class ForeignLanguageNameController {

    private final ForeignLanguageNameService foreignLanguageNameService;
    private final ForeignLanguageNameMapper foreignLanguageNameMapper;

    public ForeignLanguageNameController(
            ForeignLanguageNameService foreignLanguageNameService,
            ForeignLanguageNameMapper foreignLanguageNameMapper) {
        this.foreignLanguageNameService = foreignLanguageNameService;
        this.foreignLanguageNameMapper = foreignLanguageNameMapper;
    }

    @GetMapping("/{id}")
    public ForeignLanguageNameReadDto getById(@PathVariable Integer id) {
        return foreignLanguageNameMapper.toReadDto(foreignLanguageNameService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Map<String, Integer> deleteById(@PathVariable Integer id) {
        foreignLanguageNameService.delete(id);

        return Map.of("id", id);
    }

    @GetMapping
    public List<ForeignLanguageNameReadDto> getAll() {
        return foreignLanguageNameService.getAll()
                .stream()
                .map(foreignLanguageNameMapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ForeignLanguageNameReadDto update(
            @PathVariable Integer id,
            @RequestPart Optional<String> name,
            @RequestPart Optional<MultipartFile> flag) {
        ForeignLanguageName updatedForeignLanguageName = foreignLanguageNameService.update(
                id,
                name.orElse(null),
                getBytesFromFile(flag.orElse(null)));

        return foreignLanguageNameMapper.toReadDto(updatedForeignLanguageName);
    }

    @PostMapping
    public ForeignLanguageNameReadDto create(
            @RequestPart String name,
            @RequestPart MultipartFile flag) {
        ForeignLanguageName createdForeignLanguageName = foreignLanguageNameService.create(
                name,
                getBytesFromFile(flag));

        return foreignLanguageNameMapper.toReadDto(createdForeignLanguageName);
    }
}
