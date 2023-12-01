package com.spotit.backend.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnOkOnMainRoute() throws Exception {
        // given
        var path = "/";

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
    void shouldReturnOkOnApiRoute() throws Exception {
        // given
        var path = "/api/userAccount";

        // when
        var result = mockMvc.perform(get(path).with(jwt()));

        // then
        result.andExpect(status().isOk());
    }
}
