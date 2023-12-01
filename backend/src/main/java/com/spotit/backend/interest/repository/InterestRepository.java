package com.spotit.backend.interest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spotit.backend.interest.model.Interest;

public interface InterestRepository extends JpaRepository<Interest, Integer> {

    List<Interest> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
