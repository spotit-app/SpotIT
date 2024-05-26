package com.spotit.backend.domain.employer.address;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.domain.employer.address.AddressUtils.createAddress;
import static com.spotit.backend.domain.employer.address.AddressUtils.createAddressReadDto;
import static com.spotit.backend.domain.employer.address.AddressUtils.createAddressWriteDto;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.config.security.SecurityConfig;

@WebMvcTest(AddressController.class)
@Import(SecurityConfig.class)
class AddressControllerTest {

    static final String ADDRESS_API_URL = "/api/userAccount/auth0Id1/company/1/address";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AddressService addressService;

    @MockBean
    AddressMapper addressMapper;

    @Test
    void shouldReturnCompanyAddress() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(addressService.getByCompanyId(1))
                .thenReturn(createAddress(1));
        when(addressMapper.toReadDto(any()))
                .thenReturn(createAddressReadDto(1));

        // when
        var result = mockMvc.perform(get(ADDRESS_API_URL).with(createMockJwt(userAuth0Id)));

        // then
        verify(addressService, times(1)).getByCompanyId(1);
        verify(addressMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Country 1"));
    }

    @Test
    void shouldReturnModifiedAddress() throws Exception {
        // given
        var addressToModify = createAddressWriteDto(1);
        var modifiedAddress = createAddressReadDto(1);

        when(addressMapper.toReadDto(any())).thenReturn(modifiedAddress);

        // when
        var result = mockMvc.perform(put(ADDRESS_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addressToModify)));

        // then
        verify(addressMapper, times(1)).fromWriteDto(any());
        verify(addressService, times(1)).updateByCompanyId(any(), any());
        verify(addressMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedAddress.id()))
                .andExpect(jsonPath("$.country").value(modifiedAddress.country()));
    }

    @Test
    void shouldReturnErrorWhenEditedAddressNotFound() throws Exception {
        // given
        var addressId = 1;
        var addressToModify = createAddressWriteDto(1);
        var addressNotFoundException = new EntityNotFoundException(addressId);

        when(addressService.updateByCompanyId(any(), any()))
                .thenThrow(addressNotFoundException);

        // when
        var result = mockMvc.perform(put(ADDRESS_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(addressToModify)));

        // then
        verify(addressMapper, times(1)).fromWriteDto(any());
        verify(addressService, times(1)).updateByCompanyId(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(addressNotFoundException.getMessage()));
    }
}
