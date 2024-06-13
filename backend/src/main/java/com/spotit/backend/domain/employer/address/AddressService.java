package com.spotit.backend.domain.employer.address;

public interface AddressService {

    Address getById(Integer id);

    Address updateById(Integer addressId, Address addressToUpdate);
}
