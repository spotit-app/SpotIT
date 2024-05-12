package com.spotit.backend.domain.referenceData.workMode;

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
@RequestMapping("/api/workMode")
public class WorkModeController {

    private final WorkModeService workModeService;
    private final WorkModeMapper workModeMapper;

    public WorkModeController(
            WorkModeService workModeService,
            WorkModeMapper workModeMapper) {

        this.workModeService = workModeService;
        this.workModeMapper = workModeMapper;
    }

    @GetMapping("/{id}")
    public WorkModeReadDto getWorkModeById(@PathVariable Integer id) {
        return workModeMapper.toReadDto(workModeService.getById(id));
    }

    @GetMapping
    public List<WorkModeReadDto> getAllWorkModes() {
        return workModeService.getAll()
                .stream()
                .map(workModeMapper::toReadDto)
                .toList();
    }

    @PostMapping
    public WorkModeReadDto createWorkMode(@RequestBody WorkModeWriteDto writeDto) {
        WorkMode workMode = workModeMapper.fromWriteDto(writeDto);

        return workModeMapper.toReadDto(workModeService.create(workMode));
    }

    @PutMapping("/{id}")
    public WorkModeReadDto updateWorkMode(
            @PathVariable Integer id,
            @RequestBody WorkModeWriteDto writeDto) {
        WorkMode entityToUpdate = workModeMapper.fromWriteDto(writeDto);
        WorkMode editedEntity = workModeService.update(id, entityToUpdate);

        return workModeMapper.toReadDto(editedEntity);
    }

    @DeleteMapping("/{id}")
    public Map<String, Integer> deleteWorkModebyId(@PathVariable Integer id) {
        workModeService.delete(id);

        return Map.of("id", id);
    }
}
