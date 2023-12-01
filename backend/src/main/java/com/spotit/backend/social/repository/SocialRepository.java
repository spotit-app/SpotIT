package com.spotit.backend.social.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spotit.backend.social.model.Social;

@Repository
public interface SocialRepository extends JpaRepository<Social, Integer> {

    List<Social> findByUserAccountAuth0Id(String userAccountAuth0Id);
}
