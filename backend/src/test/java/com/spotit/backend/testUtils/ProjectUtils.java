package com.spotit.backend.testUtils;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.project.dto.ReadProjectDto;
import com.spotit.backend.project.dto.WriteProjectDto;
import com.spotit.backend.project.model.Project;

public interface ProjectUtils {

    static Project createProject(Integer id) {
        return Project.builder()
                .description("Description " + id)
                .projectUrl("projectUrl" + id)
                .build();
    }

    static ReadProjectDto createReadProjectDto(Integer id) {
        return ReadProjectDto.builder()
                .description("Description " + id)
                .projectUrl("projectUrl" + id)
                .build();
    }

    static WriteProjectDto createWriteProjectDto(Integer id) {
        return WriteProjectDto.builder()
                .description("Description " + id)
                .projectUrl("projectUrl" + id)
                .build();
    }

    static List<Project> generateProjectsList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(ProjectUtils::createProject)
                .toList();
    }
}
