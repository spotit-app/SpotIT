package com.spotit.backend.domain.employer.address;

import org.springframework.stereotype.Service;

import com.spotit.backend.abstraction.EntityNotFoundException;

@Service
public class AddressServiceImpl implements AddressService {

    protected final AddressRepository repository;

    public AddressServiceImpl(
            AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public Address getById(Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public Address updateById(Integer addressId, Address addressToUpdate) {
        Address foundAddress = getById(addressId);
        foundAddress.update(addressToUpdate);

        return repository.save(foundAddress);
    }
}
