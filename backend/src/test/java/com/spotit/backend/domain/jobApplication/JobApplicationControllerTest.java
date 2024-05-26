package com.spotit.backend.domain.jobApplication;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.domain.employer.company.CompanyUtils.createCompany;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOffer;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.createJobApplication;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.createJobApplicationReadDto;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.createJobApplicationUserReadDto;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.createJobApplicationWriteDto;
import static com.spotit.backend.domain.jobApplication.JobApplicationUtils.generateJobApplicationsList;
import static com.spotit.backend.domain.userAccount.UserAccountUtils.createUserAccount;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;
import com.spotit.backend.domain.employee.portfolio.PortfolioService;
import com.spotit.backend.domain.employer.jobOffer.JobOfferService;
import com.spotit.backend.domain.referenceData.applicationStatus.ApplicationStatus;

@WebMvcTest(JobApplicationController.class)
@Import(SecurityConfig.class)
class JobApplicationControllerTest {

    static final String JOB_APPLICATION_API_URL = "/api/jobOffer/1/application";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JobApplicationService jobApplicationService;

    @MockBean
    JobApplicationMapper jobApplicationMapper;

    @MockBean
    JobOfferService jobOfferService;

    @MockBean
    PortfolioService portfolioService;

    @Test
    void shouldReturnListOfUserJobApplications() throws Exception {
        // given
        var jobApplications = generateJobApplicationsList(3);
        Page<JobApplication> pageOfJobApplications = new PageImpl<>(jobApplications);

        when(jobApplicationService.getAllByPortfolioFilteredByStatus(any(), any(), any()))
                .thenReturn(pageOfJobApplications);
        when(jobApplicationMapper.toUserReadDto(any()))
                .thenReturn(createJobApplicationUserReadDto(1));

        // when
        var result = mockMvc.perform(get("/api/userAccount/auth0Id1/application").with(createMockJwt("auth0Id1")));

        // then
        verify(jobApplicationService,
                times(1)).getAllByPortfolioFilteredByStatus(any(), any(), any());
        verify(jobApplicationMapper, times(3)).toUserReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void shouldReturnListOfOfferJobApplications() throws Exception {
        // given
        var jobApplications = generateJobApplicationsList(3);
        Page<JobApplication> pageOfJobApplications = new PageImpl<>(jobApplications);

        when(jobApplicationService.getAllByJobOfferFilteredByStatus(any(), any(), any()))
                .thenReturn(pageOfJobApplications);
        when(jobApplicationMapper.toReadDto(any()))
                .thenReturn(createJobApplicationReadDto(1));

        // when
        var result = mockMvc.perform(
                get("/api/userAccount/auth0Id1/company/1/jobOffer/1/application").with(createMockJwt("auth0Id1")));

        // then
        verify(jobApplicationService,
                times(1)).getAllByJobOfferFilteredByStatus(any(), any(), any());
        verify(jobApplicationMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void shouldReturnCheckedOfferData() throws Exception {
        // given
        var jobApplications = generateJobApplicationsList(3);
        var jobOffer = createJobOffer(1);
        var company = createCompany(1);
        var userAccount = createUserAccount(1);
        company.setUserAccount(userAccount);
        jobOffer.setCompany(company);

        when(jobApplicationService.getAllByJobOfferAndPortfolio(any(), any()))
                .thenReturn(jobApplications);
        when(jobOfferService.getById(1)).thenReturn(jobOffer);

        // when
        var result = mockMvc.perform(
                get("/api/jobOffer/1/application/auth0Id1").with(createMockJwt("auth0Id1")));

        // then
        verify(jobApplicationService,
                times(1)).getAllByJobOfferAndPortfolio(any(), any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.hasUserApplied", is(true)))
                .andExpect(jsonPath("$.isUserOffer", is(true)));
    }

    @Test
    void shouldReturnCreatedJobApplication() throws Exception {
        // given
        var jobApplicationToCreate = createJobApplicationWriteDto(1);
        var createdJobApplication = createJobApplicationReadDto(1);

        when(jobApplicationMapper.toReadDto(any()))
                .thenReturn(createdJobApplication);

        // when
        var result = mockMvc.perform(post(JOB_APPLICATION_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jobApplicationToCreate)));

        // then
        verify(jobApplicationService, times(1)).create(any());
        verify(jobApplicationMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdJobApplication.id()))
                .andExpect(jsonPath("$.applicationStatus").value(createdJobApplication.applicationStatus()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateJobApplication() throws Exception {
        // given
        var jobApplicationToCreate = createJobApplicationWriteDto(1);
        var creatingJobApplicationException = new ErrorCreatingEntityException();

        when(jobApplicationService.create(any()))
                .thenThrow(creatingJobApplicationException);

        // when
        var result = mockMvc.perform(post(JOB_APPLICATION_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jobApplicationToCreate)));

        // then
        verify(jobApplicationService, times(1)).create(any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingJobApplicationException.getMessage()));
    }

    @Test
    void shouldChangeApplicationStatus() throws Exception {
        // given
        var jobApplicationId = 1;
        var jobApplication = createJobApplication(1);
        var createdJobApplication = createJobApplicationReadDto(1);

        when(jobApplicationService.changeStatus(jobApplicationId, ApplicationStatus.REJECTED))
                .thenReturn(jobApplication);
        when(jobApplicationMapper.toReadDto(any()))
                .thenReturn(createdJobApplication);

        // when
        var result = mockMvc.perform(put("/api/userAccount/auth0Id1/company/1/jobOffer/1/application/1/changeStatus")
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ApplicationStatus.REJECTED)));
        // then
        verify(jobApplicationService,
                times(1)).changeStatus(jobApplicationId, ApplicationStatus.REJECTED);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(jobApplication.getId()))
                .andExpect(jsonPath("$.applicationStatus").value(jobApplication.getApplicationStatus()));
    }
}
