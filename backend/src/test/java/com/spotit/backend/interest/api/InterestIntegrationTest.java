package com.spotit.backend.interest.api;

import static com.spotit.backend.testUtils.GeneralUtils.getDeletedEntityMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.testUtils.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.testUtils.InterestUtils.createInterest;
import static com.spotit.backend.testUtils.InterestUtils.generateInterestList;
import static com.spotit.backend.testUtils.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.interest.dto.WriteInterestDto;
import com.spotit.backend.interest.repository.InterestRepository;
import com.spotit.backend.userAccount.model.UserAccount;
import com.spotit.backend.userAccount.repository.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class InterestIntegrationTest {

    static final String INTEREST_API_URL = "/api/userAccount/auth0Id1/interest";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    InterestRepository interestRepository;

    @Autowired
    ObjectMapper objectMapper;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
    }

    @Test
    void shouldReturnListOfUserInterests() throws Exception {
        // given
        var interestsToCreate = generateInterestList(3);
        interestsToCreate.stream().forEach(interest -> interest.setUserAccount(userAccount));
        interestRepository.saveAll(interestsToCreate);

        // when
        var result = mockMvc.perform(get(INTEREST_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnInterestById() throws Exception {
        // given
        var interestsToCreate = generateInterestList(3);
        interestsToCreate.stream().forEach(interest -> interest.setUserAccount(userAccount));
        var createdInterests = interestRepository.saveAll(interestsToCreate);

        // when
        var result = mockMvc.perform(get(INTEREST_API_URL + "/" + createdInterests.get(1).getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdInterests.get(1).getId()))
                .andExpect(jsonPath("$.name").value(createdInterests.get(1).getName()));
    }

    @Test
    void shouldReturnErrorWhenInterestNotFound() throws Exception {
        // given
        var interestId = 1;

        // when
        var result = mockMvc.perform(get(INTEREST_API_URL + "/" + interestId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(interestId)));
    }

    @Test
    void shouldReturnCreatedInterest() throws Exception {
        // given
        var interestToCreate = new WriteInterestDto("Name 1");

        // when
        var result = mockMvc.perform(post(INTEREST_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(interestToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(interestToCreate.name()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingInterest() throws Exception {
        // given
        var interestWithTooLongName = new WriteInterestDto("example".repeat(10));

        // when
        var result = mockMvc.perform(post(INTEREST_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(interestWithTooLongName)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedInterest() throws Exception {
        // given
        var interestToCreate = createInterest(1);
        interestToCreate.setUserAccount(userAccount);
        var existingInterest = interestRepository.save(interestToCreate);
        var newInterest = new WriteInterestDto("Name 2");

        // when
        var result = mockMvc.perform(put(INTEREST_API_URL + "/" + existingInterest.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newInterest)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingInterest.getId()))
                .andExpect(jsonPath("$.name").value(newInterest.name()));
    }

    @Test
    void shouldReturnErrorWhenEditedInterestNotFound() throws Exception {
        // given
        var interestId = 1;
        var newInterest = new WriteInterestDto("Name 2");

        // when
        var result = mockMvc.perform(put(INTEREST_API_URL + "/" + interestId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newInterest)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(interestId)));
    }

    @Test
    void shouldDeleteInterest() throws Exception {
        // given
        var interestToCreate = createInterest(1);
        interestToCreate.setUserAccount(userAccount);
        var interestToDelete = interestRepository.save(interestToCreate);

        // when
        var result = mockMvc.perform(delete(INTEREST_API_URL + "/" + interestToDelete.getId()).with(jwt()));

        // then
        assertFalse(interestRepository.existsById(interestToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(getDeletedEntityMessage(interestToDelete.getId())));
    }

    @Test
    void shouldReturnErrorWhenDeletedInterestNotFound() throws Exception {
        // given
        var interestId = 1;

        // when
        var result = mockMvc.perform(delete(INTEREST_API_URL + "/" + interestId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(interestId)));
    }
}
