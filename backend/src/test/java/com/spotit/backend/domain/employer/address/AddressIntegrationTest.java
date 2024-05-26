package com.spotit.backend.domain.employer.address;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.domain.employer.address.AddressUtils.createAddressWriteDto;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.employer.company.CompanyRepository;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class AddressIntegrationTest extends IntegrationTest {

    static final String COMPANY_API_URL = "/api/userAccount/auth0Id1/company/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    UserAccount userAccount;

    Company company;

    private String getAddressUrl() {
        return COMPANY_API_URL + company.getId() + "/address";
    }

    @BeforeEach
    void setUp() {
        userAccount = userAccountRepository.save(createUserAccount(1));

        var companytoCreate = createCompany(1);
        companytoCreate.setUserAccount(userAccount);
        company = companyRepository.save(companytoCreate);
    }

    @Test
    void shouldReturnCompanyAddress() throws Exception {
        // given
        var addressUrl = getAddressUrl();

        // when
        var result = mockMvc.perform(get(addressUrl)
                .with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.country").value("Country 1"));
    }

    @Test
    void shouldReturnModifiedAddress() throws Exception {
        var addressUrl = getAddressUrl();
        var modifiedAddress = createAddressWriteDto(2);

        // when
        var result = mockMvc.perform(put(addressUrl)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedAddress)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.getAddress().getId()))
                .andExpect(jsonPath("$.country").value(modifiedAddress.country()));
    }

    @Test
    void shouldReturnErrorWhenEditedAddressNotFound() throws Exception {
        // given
        var addressId = 0;
        var modifiedAddress = createAddressWriteDto(2);

        // when
        var result = mockMvc.perform(put("/api/userAccount/auth0Id1/company/0/address")
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedAddress)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(addressId)));
    }
}
