package com.spotit.backend.domain.employer.jobOffer;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOffer;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.generateJobOffersList;
import static com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameUtils.generateForeignLanguageNameList;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.generateSoftSkillNameList;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.generateTechSkillNameList;
import static com.spotit.backend.domain.referenceData.workExperience.WorkExperienceUtils.createWorkExperience;
import static com.spotit.backend.domain.referenceData.workMode.WorkModeUtils.generateWorkModeList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.domain.employer.company.Company;
import com.spotit.backend.domain.employer.company.CompanyService;

@ExtendWith(MockitoExtension.class)
class JobOfferServiceTest {

    @Mock
    JobOfferRepository jobOfferRepository;

    @Mock
    CompanyService companyService;

    @Mock
    Company mockedCompany;

    @InjectMocks
    JobOfferServiceImpl jobOfferServiceImpl;

    @Test
    void shouldReturnAllJobOffersOfCompany() {
        // given
        var foundJobOffers = generateJobOffersList(3);
        Page<JobOffer> pageOfJobOffers = new PageImpl<>(foundJobOffers);
        Pageable pageable = PageRequest.of(0, 5);

        when(jobOfferRepository.findByCompany(mockedCompany, pageable))
                .thenReturn(pageOfJobOffers);

        // when
        Page<JobOffer> result = jobOfferServiceImpl.getAllByCompany(mockedCompany, pageable);

        // then
        verify(jobOfferRepository,
                times(1)).findByCompany(mockedCompany, pageable);

        assertEquals(foundJobOffers.size(), result.getSize());
        assertEquals(foundJobOffers, result.getContent());
    }

    @Test
    void shouldReturnJobOfferById() {
        // given
        var jobOfferId = 1;
        var foundJobOffer = createJobOffer(jobOfferId);

        when(jobOfferRepository.findById(jobOfferId))
                .thenReturn(Optional.of(foundJobOffer));

        // when
        var result = jobOfferServiceImpl.getById(jobOfferId);

        // then
        verify(jobOfferRepository, times(1)).findById(jobOfferId);

        assertEquals(foundJobOffer, result);
    }

    @Test
    void shouldThrowExceptionWhenJobOfferNotFound() {
        // given
        var jobOfferId = 1;

        when(jobOfferRepository.findById(jobOfferId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> jobOfferServiceImpl.getById(jobOfferId));

        // then
        verify(jobOfferRepository, times(1)).findById(jobOfferId);

        assertEquals(getEntityNotFoundMessage(jobOfferId), exception.getMessage());
    }

    @Test
    void shouldReturnJobOfferWhenCreatedSuccessfully() {
        // given
        var workExperience = createWorkExperience(1);
        var foreignLanguageNames = generateForeignLanguageNameList(1);
        var softSkillNames = generateSoftSkillNameList(1);
        var techSkillNames = generateTechSkillNameList(1);
        var workModes = generateWorkModeList(1);
        var company = createCompany(1);
        var jobOfferToCreate = createJobOffer(1);

        when(jobOfferRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = jobOfferServiceImpl.create(
                jobOfferToCreate, company, workExperience, techSkillNames, softSkillNames, foreignLanguageNames,
                workModes);

        // then
        verify(jobOfferRepository, times(1)).save(jobOfferToCreate);

        assertEquals(jobOfferToCreate.getName(), result.getName());
        assertEquals(jobOfferToCreate.getPosition(), result.getPosition());
        assertEquals(company, result.getCompany());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingJobOffer() {
        var workExperience = createWorkExperience(1);
        var foreignLanguageNames = generateForeignLanguageNameList(1);
        var softSkillNames = generateSoftSkillNameList(1);
        var techSkillNames = generateTechSkillNameList(1);
        var workModes = generateWorkModeList(1);
        var company = createCompany(1);
        var jobOfferToCreate = createJobOffer(1);

        when(jobOfferRepository.save(jobOfferToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> jobOfferServiceImpl.create(
                        jobOfferToCreate, company, workExperience, techSkillNames, softSkillNames, foreignLanguageNames,
                        workModes));

        // then
        verify(jobOfferRepository, times(1)).save(jobOfferToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldDeleteJobOffer() {
        // given
        var jobOfferToDeleteId = 1;
        var jobOfferToDelete = createJobOffer(jobOfferToDeleteId);

        when(jobOfferRepository.findById(jobOfferToDeleteId))
                .thenReturn(Optional.of(jobOfferToDelete));

        // when
        jobOfferServiceImpl.delete(jobOfferToDeleteId);

        // then
        verify(jobOfferRepository, times(1)).findById(jobOfferToDeleteId);
        verify(jobOfferRepository, times(1)).delete(jobOfferToDelete);
    }

    @Test
    void shouldReturnModifiedJobOffer() {
        // given
        var workExperience = createWorkExperience(1);
        var foreignLanguageNames = generateForeignLanguageNameList(1);
        var softSkillNames = generateSoftSkillNameList(1);
        var techSkillNames = generateTechSkillNameList(1);
        var workModes = generateWorkModeList(1);
        var jobOfferToModifyId = 1;
        var currentJobOffer = createJobOffer(jobOfferToModifyId);
        var modifiedJobOffer = createJobOffer(2);

        when(jobOfferRepository.findById(jobOfferToModifyId))
                .thenReturn(Optional.of(currentJobOffer));
        when(jobOfferRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = jobOfferServiceImpl.update(jobOfferToModifyId,
                modifiedJobOffer, workExperience, techSkillNames, softSkillNames, foreignLanguageNames,
                workModes);

        // then
        verify(jobOfferRepository, times(1)).findById(jobOfferToModifyId);
        verify(jobOfferRepository, times(1)).save(any());

        assertEquals(modifiedJobOffer.getName(), result.getName());
        assertEquals(modifiedJobOffer.getPosition(), result.getPosition());
    }

    @Test
    void shouldReturnUnchangedJobOfferWhenNoChanges() {
        // given
        var workExperience = createWorkExperience(1);
        var foreignLanguageNames = generateForeignLanguageNameList(1);
        var softSkillNames = generateSoftSkillNameList(1);
        var techSkillNames = generateTechSkillNameList(1);
        var workModes = generateWorkModeList(1);
        var jobOfferToModifyId = 1;
        var currentJobOffer = createJobOffer(jobOfferToModifyId);
        var modifiedJobOffer = new JobOffer();

        when(jobOfferRepository.findById(jobOfferToModifyId))
                .thenReturn(Optional.of(currentJobOffer));
        when(jobOfferRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = jobOfferServiceImpl.update(jobOfferToModifyId,
                modifiedJobOffer, workExperience, techSkillNames, softSkillNames, foreignLanguageNames,
                workModes);

        // then
        verify(jobOfferRepository, times(1)).findById(jobOfferToModifyId);
        verify(jobOfferRepository, times(1)).save(any());

        assertEquals(currentJobOffer, result);
    }
}
