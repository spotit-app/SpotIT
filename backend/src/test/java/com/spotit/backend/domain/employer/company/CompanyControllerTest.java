package com.spotit.backend.domain.employer.company;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompanyReadDto;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompanyWriteDto;
import static com.spotit.backend.domain.employer.company.CompanyUtils.generateCompanysList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;

@WebMvcTest(CompanyController.class)
@Import(SecurityConfig.class)
class CompanyControllerTest {

    static final String COMPANY_API_URL = "/api/userAccount/auth0Id1/company";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CompanyService companyService;

    @MockBean
    CompanyMapper companyMapper;

    @Test
    void shouldReturnListOfUserCompanys() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(companyService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateCompanysList(3));
        when(companyMapper.toReadDto(any()))
                .thenReturn(createCompanyReadDto(1));

        // when
        var result = mockMvc.perform(get(COMPANY_API_URL).with(createMockJwt("auth0Id1")));

        // then
        verify(companyService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(companyMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCompanyInformation() throws Exception {
        var companyId = 1;
        var companyToFind = createCompanyReadDto(1);

        when(companyMapper.toReadDto(any()))
                .thenReturn(companyToFind);

        // when
        var result = mockMvc.perform(get("/api/company/" + companyId).with(createMockJwt("auth0Id1")));

        // then
        verify(companyService, times(1)).getById(companyId);
        verify(companyMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyToFind.id()))
                .andExpect(jsonPath("$.name").value(companyToFind.name()))
                .andExpect(jsonPath("$.nip").value(companyToFind.nip()));
    }

    @Test
    void shouldReturnCreatedCompany() throws Exception {
        // given
        var companyToCreate = createCompanyWriteDto(1);
        var createdCompany = createCompanyReadDto(1);

        when(companyMapper.toReadDto(any()))
                .thenReturn(createdCompany);

        // when
        var result = mockMvc.perform(post(COMPANY_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(companyToCreate)));

        // then
        verify(companyMapper, times(1)).fromWriteDto(any());
        verify(companyService, times(1)).create(any(), any());
        verify(companyMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCompany.id()))
                .andExpect(jsonPath("$.name").value(createdCompany.name()))
                .andExpect(jsonPath("$.nip").value(createdCompany.nip()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateCompany() throws Exception {
        // given
        var companyToCreate = createCompanyWriteDto(1);
        var creatingCompanyException = new ErrorCreatingEntityException();

        when(companyService.create(any(), any()))
                .thenThrow(creatingCompanyException);

        // when
        var result = mockMvc.perform(post(COMPANY_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(companyToCreate)));

        // then
        verify(companyMapper, times(1)).fromWriteDto(companyToCreate);
        verify(companyService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingCompanyException.getMessage()));
    }

    @Test
    void shouldReturnCompanyById() throws Exception {
        // given
        var companyId = 1;
        var companyToFind = createCompanyReadDto(1);

        when(companyMapper.toReadDto(any()))
                .thenReturn(companyToFind);

        // when
        var result = mockMvc.perform(get(COMPANY_API_URL + "/" + companyId).with(createMockJwt("auth0Id1")));

        // then
        verify(companyService, times(1)).getById(companyId);
        verify(companyMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(companyToFind.id()))
                .andExpect(jsonPath("$.name").value(companyToFind.name()))
                .andExpect(jsonPath("$.nip").value(companyToFind.nip()));
    }

    @Test
    void shouldReturnErrorWhenCompanyNotFound() throws Exception {
        // given
        var companyId = 1;
        var companyNotFoundException = new EntityNotFoundException(companyId);

        when(companyService.getById(companyId))
                .thenThrow(companyNotFoundException);

        // when
        var result = mockMvc.perform(get("/api/company" + "/" + companyId).with(createMockJwt("auth0Id1")));

        // then
        verify(companyService, times(1)).getById(companyId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(companyNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedCompany() throws Exception {
        // given
        var companyId = 1;
        var companyToModify = createCompanyWriteDto(1);
        var modifiedCompany = createCompanyReadDto(1);

        when(companyMapper.toReadDto(any())).thenReturn(modifiedCompany);

        // when
        var result = mockMvc.perform(put(COMPANY_API_URL + "/" + companyId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(companyToModify)));

        // then
        verify(companyMapper, times(1)).fromWriteDto(any());
        verify(companyService, times(1)).update(any(), any(), any());
        verify(companyMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedCompany.id()))
                .andExpect(jsonPath("$.name").value(modifiedCompany.name()))
                .andExpect(jsonPath("$.nip").value(modifiedCompany.nip()));
    }

    @Test
    void shouldReturnErrorWhenEditedCompanyNotFound() throws Exception {
        // given
        var companyId = 1;
        var companyToModify = createCompanyWriteDto(1);
        var companyNotFoundException = new EntityNotFoundException(companyId);

        when(companyService.update(any(), any(), any()))
                .thenThrow(companyNotFoundException);

        // when
        var result = mockMvc.perform(put(COMPANY_API_URL + "/" + companyId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(companyToModify)));

        // then
        verify(companyMapper, times(1)).fromWriteDto(any());
        verify(companyService, times(1)).update(any(), any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(companyNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteCompany() throws Exception {
        // given
        var CompanyId = 1;

        // when
        var result = mockMvc.perform(delete(COMPANY_API_URL + "/" + CompanyId).with(createMockJwt("auth0Id1")));

        // then
        verify(companyService, times(1)).delete(CompanyId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(CompanyId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedCompanyNotFound() throws Exception {
        // given
        var CompanyId = 1;
        var CompanyNotFoundException = new EntityNotFoundException(CompanyId);

        doThrow(CompanyNotFoundException).when(companyService).delete(CompanyId);

        // when
        var result = mockMvc.perform(delete(COMPANY_API_URL + "/" + CompanyId).with(createMockJwt("auth0Id1")));

        // then
        verify(companyService, times(1)).delete(CompanyId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(CompanyNotFoundException.getMessage()));
    }
}
