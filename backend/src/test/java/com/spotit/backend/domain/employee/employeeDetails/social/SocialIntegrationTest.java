package com.spotit.backend.domain.employee.employeeDetails.social;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employee.employeeDetails.social.SocialUtils.createSocial;
import static com.spotit.backend.domain.employee.employeeDetails.social.SocialUtils.createSocialWriteDto;
import static com.spotit.backend.domain.employee.employeeDetails.social.SocialUtils.generateSocialsList;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SocialIntegrationTest extends IntegrationTest {

    static final String SOCIAL_API_URL = "/api/userAccount/auth0Id1/social";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    SocialRepository socialRepository;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
    }

    @Test
    void shouldReturnListOfUserSocials() throws Exception {
        // given
        var socialsToCreate = generateSocialsList(3);
        socialsToCreate.forEach(social -> social.setUserAccount(userAccount));
        socialRepository.saveAll(socialsToCreate);

        // when
        var result = mockMvc.perform(get(SOCIAL_API_URL).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnSocialById() throws Exception {
        // given
        var socialsToCreate = generateSocialsList(3);
        socialsToCreate.forEach(social -> social.setUserAccount(userAccount));
        var createdSocials = socialRepository.saveAll(socialsToCreate);
        var wabtedSocial = createdSocials.get(1);

        // when
        var result = mockMvc.perform(get(SOCIAL_API_URL + "/" + wabtedSocial.getId()).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wabtedSocial.getId()))
                .andExpect(jsonPath("$.name").value(wabtedSocial.getName()))
                .andExpect(jsonPath("$.socialUrl").value(wabtedSocial.getSocialUrl()));
    }

    @Test
    void shouldReturnErrorWhenSocialNotFound() throws Exception {
        // given
        var socialId = 1;

        // when
        var result = mockMvc.perform(get(SOCIAL_API_URL + "/" + socialId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(socialId)));
    }

    @Test
    void shouldReturnCreatedSocial() throws Exception {
        // given
        var socialToCreate = createSocialWriteDto(1);

        // when
        var result = mockMvc.perform(post(SOCIAL_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(socialToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(socialToCreate.name()))
                .andExpect(jsonPath("$.socialUrl").value(socialToCreate.socialUrl()));
    }

    @Test
    void shouldReturnErrorWhenErrorCreatingSocial() throws Exception {
        // given
        var socialWithTooLongUrl = SocialWriteDto.builder()
                .name("Name")
                .socialUrl("url".repeat(100))
                .build();

        // when
        var result = mockMvc.perform(post(SOCIAL_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(socialWithTooLongUrl)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedSocial() throws Exception {
        // given
        var socialToCreate = createSocial(1);
        socialToCreate.setUserAccount(userAccount);
        var existingSocial = socialRepository.save(socialToCreate);
        var modifiedSocial = createSocialWriteDto(2);

        // when
        var result = mockMvc.perform(put(SOCIAL_API_URL + "/" + existingSocial.getId())
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedSocial)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingSocial.getId()))
                .andExpect(jsonPath("$.name").value(modifiedSocial.name()))
                .andExpect(jsonPath("$.socialUrl").value(modifiedSocial.socialUrl()));
    }

    @Test
    void shouldReturnErrorWhenEditedSocialNotFound() throws Exception {
        // given
        var socialId = 1;
        var modifiedSocial = createSocialWriteDto(2);

        // when
        var result = mockMvc.perform(put(SOCIAL_API_URL + "/" + socialId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedSocial)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(socialId)));
    }

    @Test
    void shouldDeleteSocial() throws Exception {
        // given
        var socialToCreate = createSocial(1);
        socialToCreate.setUserAccount(userAccount);
        var socialToDelete = socialRepository.save(socialToCreate);

        // when
        var result = mockMvc
                .perform(delete(SOCIAL_API_URL + "/" + socialToDelete.getId()).with(createMockJwt("auth0Id1")));

        // then
        assertFalse(socialRepository.existsById(socialToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(socialToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedSocialNotFound() throws Exception {
        // given
        var socialId = 1;

        // when
        var result = mockMvc.perform(delete(SOCIAL_API_URL + "/" + socialId).with(createMockJwt("auth0Id1")));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(socialId)));
    }
}
