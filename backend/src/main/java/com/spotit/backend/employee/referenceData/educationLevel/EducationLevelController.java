package com.spotit.backend.employee.referenceData.educationLevel;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/educationLevel")
public class EducationLevelController {

    private final EducationLevelService educationLevelService;
    private final EducationLevelMapper educationLevelMapper;

    public EducationLevelController(
            EducationLevelService educationLevelService,
            EducationLevelMapper educationLevelMapper) {

        this.educationLevelService = educationLevelService;
        this.educationLevelMapper = educationLevelMapper;
    }

    @GetMapping("/{id}")
    public EducationLevelReadDto getById(@PathVariable Integer id) {
        return educationLevelMapper.toReadDto(educationLevelService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Integer deleteById(@PathVariable Integer id) {
        educationLevelService.delete(id);

        return id;
    }

    @GetMapping
    public List<EducationLevelReadDto> getAll() {
        return educationLevelService.getAll()
                .stream()
                .map(educationLevelMapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public EducationLevelReadDto update(
            @PathVariable Integer id,
            @RequestBody EducationLevelWriteDto writeDto) {
        EducationLevel entityToUpdate = educationLevelMapper.fromWriteDto(writeDto);
        EducationLevel editedEntity = educationLevelService.update(id, entityToUpdate);

        return educationLevelMapper.toReadDto(editedEntity);
    }

    @PostMapping
    public EducationLevelReadDto create(@RequestBody EducationLevelWriteDto writeEducationLevelDto) {
        EducationLevel educationLevel = EducationLevel.builder()
                .name(writeEducationLevelDto.name())
                .custom(false)
                .build();

        return educationLevelMapper.toReadDto(educationLevelService.create(educationLevel));
    }
}
