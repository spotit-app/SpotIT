package com.spotit.backend.domain.employer.company;

import java.util.List;

public interface CompanyService {

    List<Company> getAllByUserAccountAuth0Id(String userAccountAuth0Id);

    Company getById(Integer id);

    Company create(String userAccountAuth0Id, Company company);

    Company create(String userAccountAuth0Id, Company company, byte[] companyLogo);

    Company create(String userAccountAuth0Id, Company company, String companyLogoUrl);

    Company update(Integer id, Company companyToUpdate, byte[] profilePicture);

    void delete(Integer id);
}
