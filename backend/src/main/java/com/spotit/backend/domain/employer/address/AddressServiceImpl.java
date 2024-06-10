package com.spotit.backend.domain.employer.address;

import org.springframework.stereotype.Service;

import com.spotit.backend.domain.employer.company.CompanyService;

@Service
public class AddressServiceImpl implements AddressService {

    protected final AddressRepository repository;
    protected final CompanyService companyService;

    public AddressServiceImpl(
            AddressRepository repository,
            CompanyService companyService) {
        this.repository = repository;
        this.companyService = companyService;
    }

    @Override
    public Address getByCompanyId(Integer companyId) {
        return companyService.getById(companyId).getAddress();
    }

    @Override
    public Address updateByCompanyId(Integer companyId, Address addressToUpdate) {
        Address foundAddress = getByCompanyId(companyId);
        foundAddress.update(addressToUpdate);

        return repository.save(foundAddress);
    }
}
