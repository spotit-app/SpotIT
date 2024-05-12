package com.spotit.backend.domain.employer.address;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/userAccount/{auth0Id}/company/{companyId}/address")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public AddressController(AddressService addressService, AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
        this.addressService = addressService;
    }

    @GetMapping
    public AddressReadDto getAddressOfCompany(@PathVariable Integer companyId) {
        return addressMapper.toReadDto(addressService.getByCompanyId(companyId));
    }

    @PutMapping
    public AddressReadDto updateAddressOfCompany(@PathVariable Integer companyId,
            @RequestBody AddressWriteDto writeDto) {
        Address addressToUpdate = addressMapper.fromWriteDto(writeDto);
        Address editedAddress = addressService.updateByCompanyId(companyId, addressToUpdate);

        return addressMapper.toReadDto(editedAddress);
    }
}
