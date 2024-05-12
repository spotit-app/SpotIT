package com.spotit.backend.domain.employee.portfolio;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.domain.employee.portfolio.PortfolioUtils.generatePortfolio;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import com.spotit.backend.config.security.SecurityConfig;

@WebMvcTest(PortfolioController.class)
@Import(SecurityConfig.class)
class PortfolioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PortfolioService portfolioService;

    @MockBean
    PortfolioMapper portfolioMapper;

    @Test
    void shouldReturnPortfolio() throws Exception {
        // given
        var portfolioUrl = "John_Doe1";
        var readPortfolioDto = PortfolioReadDto.builder()
                .portfolioUrl(portfolioUrl)
                .build();

        when(portfolioMapper.toReadDto(any()))
                .thenReturn(readPortfolioDto);

        // when
        var result = mockMvc.perform(get("/api/portfolio/" + portfolioUrl));

        // then
        verify(portfolioService, times(1)).getByUrl(portfolioUrl);
        verify(portfolioMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioUrl").value(portfolioUrl));
    }

    @Test
    void shouldReturnPortfolioByUserAuth0Id() throws Exception {
        // given
        var portfolioUrl = "portfolio-url1";
        var auth0Id = "auth0Id1";
        var readPortfolioDto = PortfolioReadDto.builder()
                .portfolioUrl(portfolioUrl)
                .build();

        when(portfolioMapper.toReadDto(any()))
                .thenReturn(readPortfolioDto);
        when(portfolioService.getByUserAccountAuth0Id(auth0Id))
                .thenReturn(generatePortfolio(1));

        // when
        var result = mockMvc.perform(
                get("/api/userAccount/" + auth0Id + "/portfolio").with(createMockJwt(auth0Id)));

        // then
        verify(portfolioService, times(1)).getByUserAccountAuth0Id(auth0Id);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(portfolioUrl));
    }

    @Test
    void shouldReturnCreatedPortfolio() throws Exception {
        // given
        var portfolioUrl = "John_Doe1";
        var userAuth0Id = "auth0Id1";
        var createdPortfolio = PortfolioReadDto.builder()
                .portfolioUrl(portfolioUrl)
                .build();

        when(portfolioMapper.toReadDto(any())).thenReturn(createdPortfolio);

        // when
        var result = mockMvc.perform(post("/api/userAccount/" + userAuth0Id +
                "/portfolio")
                .with(createMockJwt(userAuth0Id)));

        // then
        verify(portfolioService, times(1)).create(userAuth0Id);
        verify(portfolioMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioUrl").value(portfolioUrl));
    }

    @Test
    void shouldReturnErrorWhenCantCreatePortfolio() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var invalidUserError = new InvalidUserException();

        when(portfolioService.create(userAuth0Id))
                .thenThrow(invalidUserError);

        // when
        var result = mockMvc.perform(post("/api/userAccount/" + userAuth0Id +
                "/portfolio")
                .with(createMockJwt(userAuth0Id)));

        // then
        verify(portfolioService, times(1)).create(userAuth0Id);
        verify(portfolioMapper, times(0)).toReadDto(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(invalidUserError.getMessage()));
    }

    @Test
    void shouldReturnUpdatedPortfolio() throws Exception {
        // given
        var portfolioUrl = "John_Doe1";
        var userAuth0Id = "auth0Id1";
        var updatedPortfolio = PortfolioReadDto.builder()
                .portfolioUrl(portfolioUrl)
                .build();

        when(portfolioMapper.toReadDto(any())).thenReturn(updatedPortfolio);

        // when
        var result = mockMvc.perform(put("/api/userAccount/" + userAuth0Id +
                "/portfolio")
                .with(createMockJwt(userAuth0Id)));

        // then
        verify(portfolioService, times(1)).update(userAuth0Id);
        verify(portfolioMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioUrl").value(portfolioUrl));
    }

    @Test
    void shouldReturnErrorWhenCantUpdatePortfolio() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";
        var invalidUserError = new InvalidUserException();

        when(portfolioService.update(userAuth0Id))
                .thenThrow(invalidUserError);

        // when
        var result = mockMvc.perform(put("/api/userAccount/" + userAuth0Id +
                "/portfolio")
                .with(createMockJwt(userAuth0Id)));

        // then
        verify(portfolioService, times(1)).update(userAuth0Id);
        verify(portfolioMapper, times(0)).toReadDto(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(invalidUserError.getMessage()));
    }
}
