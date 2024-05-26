package com.spotit.backend.domain.employer.address;

import static com.spotit.backend.domain.employer.address.AddressUtils.createAddress;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void shouldReturnCompanyAddress() {
        // given
        var companytoCreate = createCompany(1);
        var foundAddress = createAddress(1);

        when(companyService.getById(1))
                .thenReturn(companytoCreate);

        // when
        var result = addressServiceImpl.getByCompanyId(1);

        // then
        verify(companyService, times(1)).getById(1);
        assertEquals(foundAddress, result);
    }

    @Test
    void shouldReturnModifiedAddress() {
        // given
        var addressToModifyId = 1;
        var companytoCreate = createCompany(1);
        var modifiedAddress = createAddress(2);

        when(companyService.getById(1))
                .thenReturn(companytoCreate);
        when(addressRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = addressServiceImpl.updateByCompanyId(addressToModifyId, modifiedAddress);

        // then
        verify(companyService, times(1)).getById(1);
        verify(addressRepository, times(1)).save(any());

        assertEquals(modifiedAddress.getCountry(), result.getCountry());
        assertEquals(modifiedAddress.getCity(), result.getCity());
    }

    @Test
    void shouldReturnUnchangedAddressWhenNoChanges() {
        // given
        var addressToModifyId = 1;
        var companytoCreate = createCompany(1);
        var currentAddress = createAddress(addressToModifyId);
        var modifiedAddress = new Address();

        when(companyService.getById(1))
                .thenReturn(companytoCreate);
        when(addressRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = addressServiceImpl.updateByCompanyId(addressToModifyId, modifiedAddress);

        // then
        verify(companyService, times(1)).getById(1);
        verify(addressRepository, times(1)).save(any());

        assertEquals(currentAddress, result);
    }
}
