package com.spotit.backend.domain.employee.portfolio;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getInvalidUserMessage;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
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

import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PortfolioIntegrationTest extends IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = userAccountRepository.save(createUserAccount(1));
    }

    @Test
    void shouldReturnPortfolio() throws Exception {
        // given
        String portfolioUrl = "John_Doe1";
        var portfolioToCreate = Portfolio.builder()
                .portfolioUrl(portfolioUrl)
                .userAccount(userAccount).build();
        portfolioRepository.save(portfolioToCreate);

        // when
        var result = mockMvc.perform(get("/api/portfolio/" + portfolioUrl));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioUrl").value(portfolioUrl));
    }

    @Test
    void shouldReturnPortfolioByUserAuth0Id() throws Exception {
        // given
        String portfolioUrl = "John_Doe1";
        var portfolioToCreate = Portfolio.builder()
                .portfolioUrl(portfolioUrl)
                .userAccount(userAccount).build();
        portfolioRepository.save(portfolioToCreate);

        // when
        var result = mockMvc.perform(get("/api/userAccount/" + userAccount.getAuth0Id() + "/portfolio")
                .with(createMockJwt(userAccount.getAuth0Id())));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").value(portfolioUrl));
    }

    @Test
    void shouldReturnCreatedPortfolio() throws Exception {
        // given
        var expectedPortfolioUrl = userAccount.getFirstName() + "_" +
                userAccount.getLastName() + userAccount.getId();

        // when
        var result = mockMvc.perform(post("/api/userAccount/" +
                userAccount.getAuth0Id() + "/portfolio")
                .with(createMockJwt(userAccount.getAuth0Id())));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioUrl").value(expectedPortfolioUrl));
    }

    @Test
    void shouldReturnErrorWhenCantCreatePortfolio() throws Exception {
        // given
        userAccount.setFirstName(null);
        userAccountRepository.save(userAccount);

        // when
        var result = mockMvc.perform(post("/api/userAccount/" +
                userAccount.getAuth0Id() + "/portfolio")
                .with(createMockJwt(userAccount.getAuth0Id())));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getInvalidUserMessage()));
    }

    @Test
    void shouldReturnUpdatedPortfolio() throws Exception {
        // given
        var portfolio = Portfolio.builder()
                .portfolioUrl("url")
                .userAccount(userAccount)
                .build();
        portfolioRepository.save(portfolio);

        userAccount.setFirstName("John");
        userAccount.setLastName("Doe");
        userAccountRepository.save(userAccount);

        var expectedPortfolioUrl = userAccount.getFirstName() + "_" +
                userAccount.getLastName() + userAccount.getId();

        // when
        var result = mockMvc.perform(put("/api/userAccount/" +
                userAccount.getAuth0Id() + "/portfolio")
                .with(createMockJwt(userAccount.getAuth0Id())));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.portfolioUrl").value(expectedPortfolioUrl));
    }

    @Test
    void shouldReturnErrorWhenCantUpdatePortfolio() throws Exception {
        // given
        userAccount.setLastName(null);
        userAccountRepository.save(userAccount);

        // when
        var result = mockMvc.perform(put("/api/userAccount/" +
                userAccount.getAuth0Id() + "/portfolio")
                .with(createMockJwt(userAccount.getAuth0Id())));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getInvalidUserMessage()));
    }
}
