package com.spotit.backend.employee.referenceData.softSkillName;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/softSkillName")
public class SoftSkillNameController {

    private final SoftSkillNameService softSkillNameService;
    private final SoftSkillNameMapper softSkillNameMapper;

    public SoftSkillNameController(
            SoftSkillNameService softSkillNameService,
            SoftSkillNameMapper softSkillNameMapper) {

        this.softSkillNameService = softSkillNameService;
        this.softSkillNameMapper = softSkillNameMapper;
    }

    @GetMapping("/{id}")
    public SoftSkillNameReadDto getById(@PathVariable Integer id) {
        return softSkillNameMapper.toReadDto(softSkillNameService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Map<String, Integer> deleteById(@PathVariable Integer id) {
        softSkillNameService.delete(id);

        return Map.of("id", id);
    }

    @GetMapping
    public List<SoftSkillNameReadDto> getAll() {
        return softSkillNameService.getAll()
                .stream()
                .map(softSkillNameMapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public SoftSkillNameReadDto update(
            @PathVariable Integer id,
            @RequestBody SoftSkillNameWriteDto writeDto) {
        SoftSkillName entityToUpdate = softSkillNameMapper.fromWriteDto(writeDto);
        SoftSkillName editedEntity = softSkillNameService.update(id, entityToUpdate);

        return softSkillNameMapper.toReadDto(editedEntity);
    }

    @PostMapping
    public SoftSkillNameReadDto create(@RequestBody SoftSkillNameWriteDto writeSoftSkillNameDto) {
        SoftSkillName softSkillName = SoftSkillName.builder()
                .name(writeSoftSkillNameDto.name())
                .custom(false)
                .build();

        return softSkillNameMapper.toReadDto(softSkillNameService.create(softSkillName));
    }
}
