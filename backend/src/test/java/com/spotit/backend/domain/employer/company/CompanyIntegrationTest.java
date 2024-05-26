package com.spotit.backend.domain.employer.company;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompanyWriteDto;
import static com.spotit.backend.domain.employer.company.CompanyUtils.generateCompanysList;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountRepository;
import com.spotit.backend.storage.StorageService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CompanyIntegrationTest extends IntegrationTest {

    static final String COMPANY_API_URL = "/api/userAccount/auth0Id1/company";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    CompanyRepository companyRepository;

    @MockBean
    StorageService storageService;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
    }

    @Test
    void shouldReturnListOfUserCompanys() throws Exception {
        // given
        var companysToCreate = generateCompanysList(3);
        companysToCreate.forEach(Company -> Company.setUserAccount(userAccount));
        companyRepository.saveAll(companysToCreate);

        // when
        var result = mockMvc.perform(get(COMPANY_API_URL).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCompanyById() throws Exception {
        // given
        var companysToCreate = generateCompanysList(3);
        companysToCreate.forEach(company -> company.setUserAccount(userAccount));
        var createdCompanys = companyRepository.saveAll(companysToCreate);
        var wantedCompany = createdCompanys.get(1);

        // when
        var result = mockMvc
                .perform(get("/api/company/" + wantedCompany.getId()).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedCompany.getId()))
                .andExpect(jsonPath("$.name").value(wantedCompany.getName()))
                .andExpect(jsonPath("$.nip").value(wantedCompany.getNip()));
    }

    @Test
    void shouldReturnErrorWhenCompanyNotFound() throws Exception {
        // given
        var companyId = 1;

        // when
        var result = mockMvc.perform(get("/api/company/" + companyId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(companyId)));
    }

    @Test
    void shouldReturnCreatedCompany() throws Exception {
        // given
        var companyToCreate = createCompanyWriteDto(1);

        // when
        var result = mockMvc.perform(post(COMPANY_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(companyToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(companyToCreate.name()))
                .andExpect(jsonPath("$.nip").value(companyToCreate.nip()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingCompany() throws Exception {
        // given
        var companyWithTooLongUrl = CompanyWriteDto.builder()
                .name("Name")
                .nip("1".repeat(11))
                .build();

        // when
        var result = mockMvc.perform(post(COMPANY_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(companyWithTooLongUrl)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedCompany() throws Exception {
        // given
        var companyToCreate = createCompany(1);
        companyToCreate.setUserAccount(userAccount);
        var existingCompany = companyRepository.save(companyToCreate);
        var modifiedCompany = createCompanyWriteDto(2);

        var modifiedCompanyRequest = new MockMultipartFile(
                "writeDto",
                "writeDto",
                "application/json",
                objectMapper.writeValueAsBytes(modifiedCompany));

        // when
        var result = mockMvc.perform(multipart(PUT, COMPANY_API_URL + "/" + existingCompany.getId())
                .file(modifiedCompanyRequest)
                .with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingCompany.getId()))
                .andExpect(jsonPath("$.name").value(modifiedCompany.name()))
                .andExpect(jsonPath("$.nip").value(modifiedCompany.nip()));
    }

    @Test
    void shouldReturnErrorWhenEditedCompanyNotFound() throws Exception {
        // given
        var companyId = 1;
        var modifiedCompany = createCompanyWriteDto(2);

        // when
        var result = mockMvc.perform(put(COMPANY_API_URL + "/" + companyId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedCompany)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(companyId)));
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        // given
        var companyToDelete = createCompany(1);
        companyToDelete.setUserAccount(userAccount);
        var savedCompany = companyRepository.save(companyToDelete);

        // when
        var result = mockMvc
                .perform(delete(COMPANY_API_URL + "/" +
                        savedCompany.getId()).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(savedCompany.getId()))));

        assertFalse(companyRepository.existsById(savedCompany.getId()));
    }

    @Test
    void shouldReturnErrorWhenDeletedCompanyNotFound() throws Exception {
        // given
        var companyId = 1;

        // when
        var result = mockMvc.perform(delete(COMPANY_API_URL + "/" + companyId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(companyId)));
    }
}
