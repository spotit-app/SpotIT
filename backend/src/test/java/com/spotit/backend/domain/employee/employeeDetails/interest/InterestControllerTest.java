package com.spotit.backend.domain.employee.employeeDetails.interest;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.employee.employeeDetails.interest.InterestUtils.createInterestReadDto;
import static com.spotit.backend.domain.employee.employeeDetails.interest.InterestUtils.generateInterestList;
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

@WebMvcTest(InterestController.class)
@Import(SecurityConfig.class)
class InterestControllerTest {

    static final String INTEREST_API_URL = "/api/userAccount/auth0Id1/interest";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InterestService interestService;

    @MockBean
    InterestMapper interestMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnListOfUserInterests() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(interestService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateInterestList(3));
        when(interestMapper.toReadDto(any()))
                .thenReturn(createInterestReadDto(1));

        // when
        var result = mockMvc.perform(get(INTEREST_API_URL).with(createMockJwt("auth0Id1")));

        // then
        verify(interestService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(interestMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedInterest() throws Exception {
        // given
        var interestToCreate = new InterestWriteDto("Name 1");
        var createdInterest = createInterestReadDto(1);

        when(interestMapper.toReadDto(any()))
                .thenReturn(createdInterest);

        // when
        var result = mockMvc.perform(post(INTEREST_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(interestToCreate)));

        // then
        verify(interestService, times(1)).create(any(), any());
        verify(interestMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdInterest.id()))
                .andExpect(jsonPath("$.name").value(createdInterest.name()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateInterest() throws Exception {
        // given
        var interestToCreate = new InterestWriteDto("Name 1");
        var creatingInterestError = new ErrorCreatingEntityException();

        when(interestService.create(any(), any()))
                .thenThrow(creatingInterestError);

        // when
        var result = mockMvc.perform(post(INTEREST_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(interestToCreate)));

        // then
        verify(interestService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingInterestError.getMessage()));
    }

    @Test
    void shouldReturnInterestById() throws Exception {
        // given
        var interestId = 1;
        var interestToFind = createInterestReadDto(1);

        when(interestMapper.toReadDto(any()))
                .thenReturn(interestToFind);

        // when
        var result = mockMvc.perform(get(INTEREST_API_URL + "/" + interestId).with(createMockJwt("auth0Id1")));

        // then
        verify(interestService, times(1)).getById(interestId);
        verify(interestMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(interestToFind.id()))
                .andExpect(jsonPath("$.name").value(interestToFind.name()));
    }

    @Test
    void shouldReturnErrorWhenInterestNotFound() throws Exception {
        // given
        var interestId = 1;
        var interestNotFoundException = new EntityNotFoundException(interestId);

        when(interestService.getById(interestId))
                .thenThrow(interestNotFoundException);

        // when
        var result = mockMvc.perform(get(INTEREST_API_URL + "/" + interestId).with(createMockJwt("auth0Id1")));

        // then
        verify(interestService, times(1)).getById(interestId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(interestNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedInterest() throws Exception {
        // given
        var interestId = 1;
        var interestToModify = new InterestWriteDto("Name 2");
        var modifiedInterest = createInterestReadDto(1);

        when(interestMapper.toReadDto(any())).thenReturn(modifiedInterest);

        // when
        var result = mockMvc.perform(put(INTEREST_API_URL + "/" + interestId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(interestToModify)));

        // then
        verify(interestService, times(1)).update(any(), any());
        verify(interestMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedInterest.id()))
                .andExpect(jsonPath("$.name").value(modifiedInterest.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedInterestNotFound() throws Exception {
        // given
        var interestId = 1;
        var modifiedInterest = new InterestWriteDto("Name 2");
        var interestNotFoundException = new EntityNotFoundException(interestId);

        when(interestService.update(any(), any()))
                .thenThrow(interestNotFoundException);

        // when
        var result = mockMvc.perform(put(INTEREST_API_URL + "/" + interestId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedInterest)));

        // then
        verify(interestService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(interestNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteInterest() throws Exception {
        // given
        var interestId = 1;

        // when
        var result = mockMvc.perform(delete(INTEREST_API_URL + "/" + interestId).with(createMockJwt("auth0Id1")));

        // then
        verify(interestService, times(1)).delete(interestId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(interestId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedInterestNotFound() throws Exception {
        // given
        var interestId = 1;
        var interestNotFoundException = new EntityNotFoundException(interestId);

        doThrow(interestNotFoundException).when(interestService).delete(interestId);

        // when
        var result = mockMvc.perform(delete(INTEREST_API_URL + "/" + interestId).with(createMockJwt("auth0Id1")));

        // then
        verify(interestService, times(1)).delete(interestId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(interestNotFoundException.getMessage()));
    }
}
