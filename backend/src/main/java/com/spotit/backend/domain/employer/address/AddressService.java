package com.spotit.backend.domain.employer.address;

public interface AddressService {

    Address getByCompanyId(Integer companyId);

    Address updateByCompanyId(Integer companyId, Address addressToUpdate);
}
