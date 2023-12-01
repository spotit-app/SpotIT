package com.spotit.backend.techSkill.controller;

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
import com.spotit.backend.techSkill.dto.ReadTechSkillNameDto;
import com.spotit.backend.techSkill.dto.WriteTechSkillNameDto;
import com.spotit.backend.techSkill.mapper.TechSkillNameMapper;
import com.spotit.backend.techSkill.model.TechSkillName;
import com.spotit.backend.techSkill.service.TechSkillNameService;

@RestController
@RequestMapping("/api/techSkillName")
public class TechSkillNameController
        extends AbstractController<TechSkillName, Integer, ReadTechSkillNameDto, WriteTechSkillNameDto> {

    private final TechSkillNameService techSkillNameService;

    public TechSkillNameController(
            TechSkillNameService techSkillNameService,
            TechSkillNameMapper techSkillNameMapper) {
        super(techSkillNameService, techSkillNameMapper);
        this.techSkillNameService = techSkillNameService;
    }

    @GetMapping
    public List<ReadTechSkillNameDto> getAllTechSkillNames() {
        return techSkillNameService.getAll()
                .stream()
                .map(mapper::toReadDto)
                .toList();
    }

    @PutMapping("/{id}")
    public ReadTechSkillNameDto updateTechSkillName(
            @PathVariable Integer id,
            @RequestPart Optional<String> name,
            @RequestPart Optional<MultipartFile> logo) {
        TechSkillName updatedTechSkillName = techSkillNameService.update(
                id,
                name.orElse(null),
                getBytesFromFile(logo.orElse(null)));

        return mapper.toReadDto(updatedTechSkillName);
    }

    @PostMapping
    public ReadTechSkillNameDto createTechSkillName(
            @RequestPart String name,
            @RequestPart MultipartFile logo) {
        TechSkillName techSkillName = techSkillNameService.create(
                name,
                getBytesFromFile(logo));

        return mapper.toReadDto(techSkillNameService.create(techSkillName));
    }
}
