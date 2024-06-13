package com.spotit.backend.domain.employer.company;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static com.spotit.backend.domain.employer.company.CompanyUtils.generateCompanysList;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.spotit.backend.domain.employer.address.AddressService;
import com.spotit.backend.domain.userAccount.UserAccount;
import com.spotit.backend.domain.userAccount.UserAccountService;
import com.spotit.backend.storage.StorageService;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    UserAccountService userAccountService;

    @Mock
    UserAccount mockedUser;

    @Mock
    StorageService storageService;

    @Mock
    AddressService addressService;

    @InjectMocks
    CompanyServiceImpl companyServiceImpl;

    @Test
    void shouldReturnAllCompanysOfUser() {
        // given
        var userAccountAuth0Id = "auth0Id1";
        var foundCompanys = generateCompanysList(3);

        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(mockedUser);
        when(mockedUser.getAuth0Id())
                .thenReturn(userAccountAuth0Id);
        when(companyRepository.findByUserAccountAuth0Id(userAccountAuth0Id))
                .thenReturn(foundCompanys);

        // when
        var result = companyServiceImpl.getAllByUserAccountAuth0Id(userAccountAuth0Id);

        // then
        verify(companyRepository, times(1)).findByUserAccountAuth0Id(userAccountAuth0Id);

        assertEquals(foundCompanys.size(), result.size());
        assertEquals(foundCompanys.get(0), result.get(0));
        assertEquals(foundCompanys.get(1), result.get(1));
        assertEquals(foundCompanys.get(2), result.get(2));
    }

    @Test
    void shouldReturnCompanyById() {
        // given
        var companyId = 1;
        var foundCompany = createCompany(companyId);

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.of(foundCompany));

        // when
        var result = companyServiceImpl.getById(companyId);

        // then
        verify(companyRepository, times(1)).findById(companyId);

        assertEquals(foundCompany, result);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNotFound() {
        // given
        var companyId = 1;

        when(companyRepository.findById(companyId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> companyServiceImpl.getById(companyId));

        // then
        verify(companyRepository, times(1)).findById(companyId);

        assertEquals(getEntityNotFoundMessage(companyId), exception.getMessage());
    }

    @Test
    void shouldReturnCompanyWhenCreatedSuccessfully() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var companyToCreate = createCompany(1);

        when(companyRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var result = companyServiceImpl.create(userAccountAuth0Id, companyToCreate);

        // then
        verify(companyRepository, times(1)).save(companyToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(companyToCreate.getName(), result.getName());
        assertEquals(companyToCreate.getNip(), result.getNip());
        assertEquals(foundUserAccount, result.getUserAccount());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingCompany() {
        // given
        var foundUserAccount = createUserAccount(1);
        var userAccountAuth0Id = foundUserAccount.getAuth0Id();
        var companyToCreate = createCompany(1);

        when(companyRepository.save(companyToCreate))
                .thenThrow(IllegalArgumentException.class);
        when(userAccountService.getByAuth0Id(userAccountAuth0Id))
                .thenReturn(foundUserAccount);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> companyServiceImpl.create(userAccountAuth0Id, companyToCreate));

        // then
        verify(companyRepository, times(1)).save(companyToCreate);
        verify(userAccountService, times(1)).getByAuth0Id(userAccountAuth0Id);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteCompany() {
        // given
        var companyToDeleteId = 1;
        var companyToDelete = createCompany(companyToDeleteId);

        when(companyRepository.findById(companyToDeleteId))
                .thenReturn(Optional.of(companyToDelete));

        // when
        companyServiceImpl.delete(companyToDeleteId);

        // then
        verify(companyRepository, times(1)).findById(companyToDeleteId);
        verify(companyRepository, times(1)).delete(companyToDelete);
    }

    @Test
    void shouldReturnModifiedCompany() {
        // given
        var companyToModifyId = 1;
        var currentCompany = createCompany(companyToModifyId);
        var modifiedCompany = createCompany(2);
        var companyPicture = new byte[0];

        when(companyRepository.findById(companyToModifyId))
                .thenReturn(Optional.of(currentCompany));
        when(storageService.uploadFile(any(), any(), any()))
                .thenReturn("http://profilePicture.url");
        when(companyRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = companyServiceImpl.update(companyToModifyId, modifiedCompany, companyPicture);

        // then
        verify(storageService, times(1)).uploadFile(any(), any(), any());
        verify(companyRepository, times(1)).findById(companyToModifyId);
        verify(companyRepository, times(1)).save(any());
        verify(addressService, times(1)).updateById(any(), any());

        assertEquals(modifiedCompany.getName(), result.getName());
        assertEquals(modifiedCompany.getNip(), result.getNip());
    }

    @Test
    void shouldReturnUnchangedCompanyWhenNoChanges() {
        // given
        var companyToModifyId = 1;
        var currentCompany = createCompany(companyToModifyId);
        var modifiedCompany = new Company();

        when(companyRepository.findById(companyToModifyId))
                .thenReturn(Optional.of(currentCompany));
        when(companyRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = companyServiceImpl.update(companyToModifyId, modifiedCompany, null);

        // then
        verify(companyRepository, times(1)).findById(companyToModifyId);
        verify(companyRepository, times(1)).save(any());

        assertEquals(currentCompany, result);
    }
}
