package com.spotit.backend.employee.userDetails.project;

import java.util.List;
import java.util.stream.IntStream;

public interface ProjectUtils {

    static Project createProject(Integer id) {
        return Project.builder()
                .name("Name " + id)
                .description("Description " + id)
                .projectUrl("projectUrl" + id)
                .build();
    }

    static ProjectReadDto createProjectReadDto(Integer id) {
        return ProjectReadDto.builder()
                .name("Name " + id)
                .description("Description " + id)
                .projectUrl("projectUrl" + id)
                .build();
    }

    static ProjectWriteDto createProjectWriteDto(Integer id) {
        return ProjectWriteDto.builder()
                .name("Name " + id)
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
