package com.spotit.backend.employee.referenceData.techSkillName;

import static com.spotit.backend.utils.FileUtils.getBytesFromFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/techSkillName")
public class TechSkillNameController {

    private final TechSkillNameService techSkillNameService;
    private final TechSkillNameMapper techSkillNameMapper;

    public TechSkillNameController(
            TechSkillNameService techSkillNameService,
            TechSkillNameMapper techSkillNameMapper) {
        this.techSkillNameService = techSkillNameService;
        this.techSkillNameMapper = techSkillNameMapper;
    }

    @GetMapping("/{id}")
    public TechSkillNameReadDto getById(@PathVariable Integer id) {
        return techSkillNameMapper.toReadDto(techSkillNameService.getById(id));
    }

    @DeleteMapping("/{id}")
    public Map<String, Integer> deleteById(@PathVariable Integer id) {
        techSkillNameService.delete(id);

        return Map.of("id", id);
    }

    @GetMapping
    public List<TechSkillNameReadDto> getAll() {
        return techSkillNameService.getAll()
                .stream()
                .map(techSkillNameMapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public TechSkillNameReadDto update(
            @PathVariable Integer id,
            @RequestPart Optional<String> name,
            @RequestPart Optional<MultipartFile> logo) {
        TechSkillName updatedTechSkillName = techSkillNameService.update(
                id,
                name.orElse(null),
                getBytesFromFile(logo.orElse(null)));

        return techSkillNameMapper.toReadDto(updatedTechSkillName);
    }

    @PostMapping
    public TechSkillNameReadDto create(
            @RequestPart String name,
            @RequestPart MultipartFile logo) {
        TechSkillName techSkillName = techSkillNameService.create(
                name,
                getBytesFromFile(logo));

        return techSkillNameMapper.toReadDto(techSkillNameService.create(techSkillName));
    }
}
