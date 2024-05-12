package com.spotit.backend.domain.employee.portfolio;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employee.portfolio.PortfolioUtils.generatePortfolio;
import static com.spotit.backend.domain.employee.portfolio.PortfolioUtils.getInvalidUserMessage;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountService;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    PortfolioRepository portfolioRepository;

    @Mock
    UserAccountService userAccountService;

    @InjectMocks
    PortfolioServiceImpl portfolioServiceImpl;

    @Test
    void shouldReturnPortfolioByUrl() {
        // given
        var portfolioToFind = generatePortfolio(1);

        when(portfolioRepository.findByPortfolioUrl(portfolioToFind.getPortfolioUrl()))
                .thenReturn(Optional.of(portfolioToFind));

        // when
        var result = portfolioServiceImpl.getByUrl(portfolioToFind.getPortfolioUrl());

        // then
        verify(portfolioRepository, times(1))
                .findByPortfolioUrl(portfolioToFind.getPortfolioUrl());

        assertEquals(portfolioToFind, result);
    }

    @Test
    void shouldThrowErrorWhenPortfolioNotFound() {
        // given
        var portfolioToFind = generatePortfolio(1);

        when(portfolioRepository.findByPortfolioUrl(portfolioToFind.getPortfolioUrl()))
                .thenReturn(Optional.empty());

        // when
        var result = assertThrows(
                EntityNotFoundException.class,
                () -> portfolioServiceImpl.getByUrl(portfolioToFind.getPortfolioUrl()));

        // then
        verify(portfolioRepository, times(1))
                .findByPortfolioUrl(portfolioToFind.getPortfolioUrl());

        assertEquals(
                getEntityNotFoundMessage(portfolioToFind.getPortfolioUrl()),
                result.getMessage());
    }

    @Test
    void shouldReturnPortfolioByUserAuth0Id() {
        // given
        var portfolioToFind = generatePortfolio(1);

        when(portfolioRepository.findByUserAccountAuth0Id("auth0Id1"))
                .thenReturn(Optional.of(portfolioToFind));

        // when
        var result = portfolioServiceImpl.getByUserAccountAuth0Id("auth0Id1");

        // then
        verify(portfolioRepository, times(1))
                .findByUserAccountAuth0Id("auth0Id1");

        assertEquals(portfolioToFind, result);
    }

    @Test
    void shouldThrowErrorWhenPortfolioByUserAuth0IdNotFound() {
        // given
        when(portfolioRepository.findByUserAccountAuth0Id("auth0Id1"))
                .thenReturn(Optional.empty());

        // when
        var result = assertThrows(
                EntityNotFoundException.class,
                () -> portfolioServiceImpl.getByUserAccountAuth0Id("auth0Id1"));

        // then
        verify(portfolioRepository, times(1))
                .findByUserAccountAuth0Id("auth0Id1");

        assertEquals(
                getEntityNotFoundMessage("auth0Id1"),
                result.getMessage());
    }

    @Test
    void shouldCreatePortfolio() {
        // given
        var userId = "auth0Id";
        var exampleUser = UserAccount.builder()
                .id(1)
                .auth0Id(userId)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userAccountService.getByAuth0Id(userId))
                .thenReturn(exampleUser);
        when(portfolioRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = portfolioServiceImpl.create(userId);

        // then
        verify(userAccountService, times(1)).getByAuth0Id(userId);
        verify(portfolioRepository, times(1)).save(any());

        assertEquals(exampleUser, result.getUserAccount());
        assertEquals("John_Doe1", result.getPortfolioUrl());
    }

    @Test
    void shouldThrowErrorWhenErrorCreatingPortfolio() {
        // given
        var userId = "auth0Id";
        var exampleUser = UserAccount.builder()
                .id(1)
                .auth0Id(userId)
                .firstName("John")
                .lastName("Doe")
                .build();

        when(userAccountService.getByAuth0Id(userId))
                .thenReturn(exampleUser);
        when(portfolioRepository.save(any()))
                .thenThrow(IllegalArgumentException.class);

        // when
        var result = assertThrows(
                ErrorCreatingEntityException.class,
                () -> portfolioServiceImpl.create(userId));

        // then
        verify(userAccountService, times(1)).getByAuth0Id(userId);
        verify(portfolioRepository, times(1)).save(any());

        assertEquals(getErrorCreatingEntityMessage(), result.getMessage());
    }

    @Test
    void shouldThrowErrorWhenInvalidUserWhileCreating() {
        // given
        var userId = "auth0Id";
        var exampleUser = UserAccount.builder()
                .id(1)
                .firstName("John")
                .auth0Id(userId)
                .build();

        when(userAccountService.getByAuth0Id(userId))
                .thenReturn(exampleUser);

        // when
        var result = assertThrows(
                InvalidUserException.class,
                () -> portfolioServiceImpl.create(userId));

        // then
        verify(userAccountService, times(1)).getByAuth0Id(userId);

        assertEquals(getInvalidUserMessage(), result.getMessage());
    }

    @Test
    void shouldUpdatePortfolio() {
        // given
        var userId = "auth0Id";
        var existingPortfolio = generatePortfolio(1);
        var exampleUser = UserAccount.builder()
                .id(1)
                .firstName("John")
                .lastName("Doe")
                .auth0Id(userId)
                .build();

        when(userAccountService.getByAuth0Id(userId))
                .thenReturn(exampleUser);
        when(portfolioRepository.findByUserAccountAuth0Id(userId))
                .thenReturn(Optional.of(existingPortfolio));
        when(portfolioRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = portfolioServiceImpl.update(userId);

        // then
        verify(userAccountService, times(1))
                .getByAuth0Id(userId);
        verify(portfolioRepository, times(1))
                .save(any());

        assertEquals("John_Doe1", result.getPortfolioUrl());
    }

    @Test
    void shouldThrowErrorWhenInvalidUserWhileUpdating() {
        // given
        var userId = "auth0Id";
        var exampleUser = UserAccount.builder()
                .id(1)
                .lastName("John")
                .auth0Id(userId)
                .build();

        when(userAccountService.getByAuth0Id(userId))
                .thenReturn(exampleUser);

        // when
        var result = assertThrows(
                InvalidUserException.class,
                () -> portfolioServiceImpl.update(userId));

        // then
        verify(userAccountService, times(1)).getByAuth0Id(userId);

        assertEquals(getInvalidUserMessage(), result.getMessage());
    }
}
