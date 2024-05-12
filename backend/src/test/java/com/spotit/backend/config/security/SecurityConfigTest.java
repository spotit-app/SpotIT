package com.spotit.backend.config.security;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.spotit.backend.abstraction.IntegrationTest;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class SecurityConfigTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnOkOnMainRoute() throws Exception {
        // given
        var path = "/api";

        // when
        var result = mockMvc.perform(get(path));

        // then
        result.andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedOnApiRoute() throws Exception {
        // given
        var path = "/api/userAccount";

        // when
        var result = mockMvc.perform(get(path));

        // then
        result.andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenAuth0IdsDontMatch() throws Exception {
        // given
        var path = "/api/userAccount/auth0Id1";

        // when
        var result = mockMvc.perform(get(path).with(createMockJwt("auth0Id2")));

        // then
        result.andExpect(status().isForbidden());
    }
}
