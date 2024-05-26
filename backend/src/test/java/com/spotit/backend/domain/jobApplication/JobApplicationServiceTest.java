package com.spotit.backend.domain.jobApplication;

import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.createJobApplication;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.generateJobApplicationsList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import com.spotit.backend.domain.employee.portfolio.Portfolio;
import com.spotit.backend.domain.employer.jobOffer.JobOffer;
import com.spotit.backend.domain.employer.jobOffer.JobOfferService;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    JobApplicationRepository jobApplicationRepository;

    @Mock
    JobOfferService JobOfferService;

    @Mock
    JobOffer mockedJobOffer;

    @Mock
    Portfolio mockedPortfolio;

    @InjectMocks
    JobApplicationServiceImpl jobApplicationServiceImpl;

    @Test
    void shouldReturnAllJobApplicationsOfJobOffer() {
        // given
        var foundJobApplications = generateJobApplicationsList(3);
        Page<JobApplication> pageOfJobApplications = new PageImpl<>(foundJobApplications);
        Pageable pageable = PageRequest.of(0, 5);

        when(jobApplicationRepository.findByJobOffer(mockedJobOffer, pageable))
                .thenReturn(pageOfJobApplications);

        // when
        Page<JobApplication> result = jobApplicationServiceImpl.getAllByJobOfferFilteredByStatus(mockedJobOffer,
                Optional.empty(),
                pageable);

        // then
        verify(jobApplicationRepository,
                times(1)).findByJobOffer(mockedJobOffer, pageable);

        assertEquals(foundJobApplications.size(), result.getSize());
        assertEquals(foundJobApplications, result.getContent());
    }

    @Test
    void hasUserApplied() {
        // given
        var foundJobApplications = generateJobApplicationsList(1);

        when(jobApplicationRepository.findByJobOfferAndPortfolio(mockedJobOffer, mockedPortfolio))
                .thenReturn(foundJobApplications);

        // when
        List<JobApplication> result = jobApplicationServiceImpl.getAllByJobOfferAndPortfolio(mockedJobOffer,
                mockedPortfolio);

        // then
        verify(jobApplicationRepository,
                times(1)).findByJobOfferAndPortfolio(mockedJobOffer, mockedPortfolio);

        assertEquals(foundJobApplications.size(), result.size());
        assertEquals(foundJobApplications.get(0), result.get(0));
    }

    @Test
    void shouldReturnAllJobApplicationsOfUser() {
        // given
        var foundJobApplications = generateJobApplicationsList(3);
        Page<JobApplication> pageOfJobApplications = new PageImpl<>(foundJobApplications);
        Pageable pageable = PageRequest.of(0, 5);

        when(jobApplicationRepository.findByPortfolio(mockedPortfolio, pageable))
                .thenReturn(pageOfJobApplications);

        // when
        Page<JobApplication> result = jobApplicationServiceImpl.getAllByPortfolioFilteredByStatus(mockedPortfolio,
                Optional.empty(),
                pageable);

        // then
        verify(jobApplicationRepository,
                times(1)).findByPortfolio(mockedPortfolio, pageable);

        assertEquals(foundJobApplications.size(), result.getSize());
        assertEquals(foundJobApplications, result.getContent());
    }

    @Test
    void shouldReturnJobApplicationById() {
        // given
        var jobApplicationId = 1;
        var foundJobApplication = createJobApplication(jobApplicationId);

        when(jobApplicationRepository.findById(jobApplicationId))
                .thenReturn(Optional.of(foundJobApplication));

        // when
        var result = jobApplicationServiceImpl.getById(jobApplicationId);

        // then
        verify(jobApplicationRepository, times(1)).findById(jobApplicationId);

        assertEquals(foundJobApplication, result);
    }

    @Test
    void shouldThrowExceptionWhenJobApplicationNotFound() {
        // given
        var jobApplicationId = 1;
        when(jobApplicationRepository.findById(jobApplicationId))
                .thenReturn(Optional.empty());

        // when
        var exception = assertThrows(
                EntityNotFoundException.class,
                () -> jobApplicationServiceImpl.getById(jobApplicationId));
        // then
        verify(jobApplicationRepository, times(1)).findById(jobApplicationId);

        assertEquals(getEntityNotFoundMessage(jobApplicationId), exception.getMessage());
    }

    @Test
    void shouldReturnJobApplicationWhenCreatedSuccessfully() {
        // given
        var jobApplicationToCreate = createJobApplication(1);

        when(jobApplicationRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        var result = jobApplicationServiceImpl.create(jobApplicationToCreate);

        // then
        verify(jobApplicationRepository, times(1)).save(jobApplicationToCreate);

        assertEquals(jobApplicationToCreate.getJobOffer(), result.getJobOffer());
        assertEquals(jobApplicationToCreate.getPortfolio(), result.getPortfolio());
    }

    @Test
    void shouldThrowExceptionWhenErrorCreatingJobApplication() {
        var jobApplicationToCreate = createJobApplication(1);

        when(jobApplicationRepository.save(jobApplicationToCreate))
                .thenThrow(IllegalArgumentException.class);

        // when
        // when
        var exception = assertThrows(
                ErrorCreatingEntityException.class,
                () -> jobApplicationServiceImpl.create(jobApplicationToCreate));

        // then
        verify(jobApplicationRepository, times(1)).save(jobApplicationToCreate);

        assertEquals(getErrorCreatingEntityMessage(), exception.getMessage());
    }

    @Test
    void shouldChangeJobApplicationStatus() {
        // given
        var jobApplicationId = 1;
        var jobApplicationToCreate = createJobApplication(1);

        when(jobApplicationRepository.findById(jobApplicationId))
                .thenReturn(Optional.of(jobApplicationToCreate));

        when(jobApplicationRepository.save(any()))
                .thenAnswer(i -> i.getArguments()[0]);

        // when
        JobApplication result = jobApplicationServiceImpl.changeStatus(jobApplicationId, ApplicationStatus.DELIVERED);

        // then
        verify(jobApplicationRepository, times(1)).save(jobApplicationToCreate);
        verify(jobApplicationRepository, times(1)).findById(jobApplicationId);

        assertEquals(jobApplicationToCreate.getJobOffer(), result.getJobOffer());
        assertEquals(jobApplicationToCreate.getPortfolio(), result.getPortfolio());
    }
}
