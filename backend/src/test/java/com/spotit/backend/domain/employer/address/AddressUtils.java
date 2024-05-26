package com.spotit.backend.domain.employer.address;

import java.util.List;
import java.util.stream.IntStream;

public interface AddressUtils {

    static Address createAddress(Integer id) {
        return Address.builder()
                .id(id)
                .country("Country " + id)
                .city("City " + id)
                .street("Street " + id)
                .zipCode("80-283 " + id)
                .build();
    }

    static AddressReadDto createAddressReadDto(Integer id) {
        return AddressReadDto.builder()
                .country("Country " + id)
                .city("City " + id)
                .street("Street " + id)
                .zipCode("80-283 " + id)
                .build();
    }

    static AddressWriteDto createAddressWriteDto(Integer id) {
        return AddressWriteDto.builder()
                .country("Country " + id)
                .city("City " + id)
                .street("Street " + id)
                .zipCode("80-283 " + id)
                .build();
    }

    static List<Address> generateAddressList(Integer length) {
        return IntStream.range(0, length)
                .mapToObj(AddressUtils::createAddress)
                .toList();
    }
}
