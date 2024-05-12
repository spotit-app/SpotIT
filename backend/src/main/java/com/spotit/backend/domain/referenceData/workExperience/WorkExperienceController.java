package com.spotit.backend.domain.referenceData.workExperience;

import java.util.List;
import java.util.Map;

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
@RequestMapping("/api/workExperience")
public class WorkExperienceController {

    private final WorkExperienceService workExperienceService;
    private final WorkExperienceMapper workExperienceMapper;

    public WorkExperienceController(
            WorkExperienceService workExperienceService,
            WorkExperienceMapper workExperienceMapper) {

        this.workExperienceService = workExperienceService;
        this.workExperienceMapper = workExperienceMapper;
    }

    @GetMapping("/{id}")
    public WorkExperienceReadDto getWorkExperienceById(@PathVariable Integer id) {
        return workExperienceMapper.toReadDto(workExperienceService.getById(id));
    }

    @GetMapping
    public List<WorkExperienceReadDto> getAllWorkExperiences() {
        return workExperienceService.getAll()
                .stream()
                .map(workExperienceMapper::toReadDto)
                .toList();
    }

    @PostMapping
    public WorkExperienceReadDto createWorkExperience(@RequestBody WorkExperienceWriteDto writeDto) {
        WorkExperience workExperience = workExperienceMapper.fromWriteDto(writeDto);

        return workExperienceMapper.toReadDto(workExperienceService.create(workExperience));
    }

    @PutMapping("/{id}")
    public WorkExperienceReadDto updateWorkExperience(
            @PathVariable Integer id,
            @RequestBody WorkExperienceWriteDto writeDto) {
        WorkExperience entityToUpdate = workExperienceMapper.fromWriteDto(writeDto);
        WorkExperience editedEntity = workExperienceService.update(id, entityToUpdate);

        return workExperienceMapper.toReadDto(editedEntity);
    }

    @DeleteMapping("/{id}")
    public Map<String, Integer> deleteWorkExperienceById(@PathVariable Integer id) {
        workExperienceService.delete(id);

        return Map.of("id", id);
    }
}
