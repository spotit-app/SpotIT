package com.spotit.backend.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotit.backend.project.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    List<Project> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
