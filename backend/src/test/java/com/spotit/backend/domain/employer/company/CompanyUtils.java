package com.spotit.backend.domain.employer.company;

import static com.spotit.backend.domain.employer.address.AddressUtils.createAddress;
import static com.spotit.backend.domain.employer.address.AddressUtils.createAddressReadDto;
import static com.spotit.backend.domain.employer.address.AddressUtils.createAddressWriteDto;

import java.util.List;
import java.util.stream.IntStream;

import com.spotit.backend.domain.employer.address.Address;
import com.spotit.backend.domain.employer.address.AddressReadDto;
import com.spotit.backend.domain.employer.address.AddressWriteDto;

public interface CompanyUtils {

    static Company createCompany(Integer id) {
        Address address = createAddress(id);

        return Company.builder()
                .id(id)
                .name("Name " + id)
                .nip("123456789" + id)
                .regon("12345678" + id)
                .logoUrl("logoUrl" + id)
                .websiteUrl("websiteUrl" + id)
                .address(address)
                .build();
    }

    static CompanyReadDto createCompanyReadDto(Integer id) {
        AddressReadDto addressReadDto = createAddressReadDto(id);

        return CompanyReadDto.builder()
                .name("Name " + id)
                .nip("123456789" + id)
                .regon("12345678" + id)
                .logoUrl("logoUrl" + id)
                .websiteUrl("websiteUrl" + id)
                .address(addressReadDto)
                .build();
    }

    static CompanyWriteDto createCompanyWriteDto(Integer id) {
        AddressWriteDto addressWriteDto = createAddressWriteDto(id);

        return CompanyWriteDto.builder()
                .name("Name " + id)
                .nip("123456789" + id)
                .regon("12345678" + id)
                .websiteUrl("websiteUrl" + id)
                .address(addressWriteDto)
                .build();
    }

    static List<Company> generateCompanysList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(CompanyUtils::createCompany)
                .toList();
    }
}
