package com.spotit.backend.domain.employer.company;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    List<Company> findByUserAccountAuth0Id(String auth0Id);
}
