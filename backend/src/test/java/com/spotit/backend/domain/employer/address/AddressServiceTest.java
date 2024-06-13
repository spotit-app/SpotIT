package com.spotit.backend.domain.employer.address;

import static com.spotit.backend.domain.employer.address.AddressUtils.createAddress;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spotit.backend.domain.employer.company.CompanyRepository;
import com.spotit.backend.domain.employer.company.CompanyService;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    AddressRepository addressRepository;

    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    AddressServiceImpl addressServiceImpl;

    @Mock
    CompanyService companyService;

    @Test
    void shouldReturnJobOfferById() {
        // given
        var addressId = 1;
        var foundAddress = createAddress(addressId);

        when(addressRepository.findById(addressId))
                .thenReturn(Optional.of(foundAddress));

        // when
        var result = addressServiceImpl.getById(addressId);

        // then
        verify(addressRepository, times(1)).findById(addressId);

        assertEquals(foundAddress, result);
    }

    @Test
    void shouldReturnModifiedAddress() {
        // given
        var addressToModifyId = 1;
        var currentAddress = createAddress(addressToModifyId);
        var modifiedAddress = createAddress(2);

        when(addressRepository.findById(1))
                .thenReturn(Optional.of(currentAddress));
        when(addressRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = addressServiceImpl.updateById(addressToModifyId, modifiedAddress);

        // then
        verify(addressRepository, times(1)).findById(addressToModifyId);
        verify(addressRepository, times(1)).save(any());

        assertEquals(modifiedAddress.getCountry(), result.getCountry());
        assertEquals(modifiedAddress.getCity(), result.getCity());
    }

    @Test
    void shouldReturnUnchangedAddressWhenNoChanges() {
        // given
        var addressToModifyId = 1;
        var currentAddress = createAddress(addressToModifyId);
        var modifiedAddress = new Address();

        when(addressRepository.findById(1))
                .thenReturn(Optional.of(currentAddress));
        when(addressRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = addressServiceImpl.updateById(addressToModifyId, modifiedAddress);

        // then
        verify(addressRepository, times(1)).findById(addressToModifyId);
        verify(addressRepository, times(1)).save(any());

        assertEquals(currentAddress, result);
    }
}
