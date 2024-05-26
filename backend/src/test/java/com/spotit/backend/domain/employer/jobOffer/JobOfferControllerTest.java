package com.spotit.backend.domain.employer.jobOffer;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOffer;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOfferListReadDto;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOfferReadDto;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.createJobOfferWriteDto;
import static com.spotit.backend.domain.employer.jobOffer.JobOfferUtils.generateJobOffersList;
import static com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameUtils.createSoftSkillName;
import static com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameUtils.createTechSkillName;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;
import com.spotit.backend.domain.employer.company.CompanyService;
import com.spotit.backend.domain.referenceData.foreignLanguageName.ForeignLanguageNameService;
import com.spotit.backend.domain.referenceData.softSkillName.SoftSkillNameService;
import com.spotit.backend.domain.referenceData.techSkillName.TechSkillNameService;
import com.spotit.backend.domain.referenceData.workExperience.WorkExperienceService;
import com.spotit.backend.domain.referenceData.workMode.WorkModeService;

@WebMvcTest(JobOfferController.class)
@Import(SecurityConfig.class)
class JobOfferControllerTest {

    static final String JOB_OFFER_API_URL = "/api/userAccount/auth0Id1/company/1/jobOffer";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    JobOfferService jobOfferService;

    @MockBean
    JobOfferMapper jobOfferMapper;

    @MockBean
    CompanyService companyService;

    @MockBean
    WorkExperienceService workExperienceService;

    @MockBean
    TechSkillNameService techSkillNameService;

    @MockBean
    SoftSkillNameService softSkillNameService;

    @MockBean
    ForeignLanguageNameService foreignLanguageNameService;

    @MockBean
    WorkModeService workModeService;

    @Test
    void shouldReturnJobOffersOfCompany() throws Exception {
        // given
        var jobOffers = generateJobOffersList(3);
        Page<JobOffer> pageOfJobOffers = new PageImpl<>(jobOffers);

        when(jobOfferService.getAllByCompany(any(), any()))
                .thenReturn(pageOfJobOffers);
        when(jobOfferMapper.toReadDto(any()))
                .thenReturn(createJobOfferReadDto(1));

        // when
        var result = mockMvc.perform(get("/api/company/1/jobOffer").with(createMockJwt("auth0Id1")));

        // then
        verify(jobOfferService,
                times(1)).getAllByCompany(any(), any());
        verify(jobOfferMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void shouldReturnJobOffers() throws Exception {
        // given
        var jobOffers = generateJobOffersList(3);
        Page<JobOffer> pageOfJobOffers = new PageImpl<>(jobOffers);

        when(jobOfferService.findByCriteria(any(), any()))
                .thenReturn(pageOfJobOffers);
        when(jobOfferMapper.toListReadDto(any()))
                .thenReturn(createJobOfferListReadDto(1));

        // when
        var result = mockMvc.perform(get("/api/jobOffer").with(createMockJwt("auth0Id1")));

        // then
        verify(jobOfferService,
                times(1)).findByCriteria(any(), any());
        verify(jobOfferMapper, times(3)).toListReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)));
    }

    @Test
    void shouldReturnJobOfferById() throws Exception {
        // given
        var jobOfferId = 1;

        when(jobOfferService.getById(jobOfferId))
                .thenReturn(createJobOffer(1));
        when(jobOfferMapper.toReadDto(any()))
                .thenReturn(createJobOfferReadDto(1));

        // when
        var result = mockMvc.perform(get("/api/jobOffer/" + jobOfferId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(jobOfferService, times(1)).getById(jobOfferId);
        verify(jobOfferMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldReturnCreatedJobOffer() throws Exception {
        // given
        var jobOfferToCreate = createJobOfferWriteDto(1);
        var createdJobOffer = createJobOfferReadDto(1);

        when(jobOfferMapper.toReadDto(any())).thenReturn(createdJobOffer);
        when(techSkillNameService.getByName(any())).thenReturn(Optional.of(createTechSkillName(1)));
        when(softSkillNameService.getByName(any())).thenReturn(Optional.of(createSoftSkillName(1)));

        // when
        var result = mockMvc.perform(post(JOB_OFFER_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jobOfferToCreate)));

        // then
        verify(jobOfferMapper, times(1)).fromWriteDto(any());
        verify(jobOfferService, times(1)).create(any(), any(), any(), any(), any(), any(), any());
        verify(jobOfferMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdJobOffer.id()))
                .andExpect(jsonPath("$.name").value(createdJobOffer.name()))
                .andExpect(jsonPath("$.position").value(createdJobOffer.position()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateJobOffer() throws Exception {
        // given
        var jobOfferToCreate = createJobOfferWriteDto(1);
        var creatingJobOfferException = new ErrorCreatingEntityException();

        when(jobOfferService.create(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(creatingJobOfferException);
        when(techSkillNameService.getByName(any())).thenReturn(Optional.of(createTechSkillName(1)));
        when(softSkillNameService.getByName(any())).thenReturn(Optional.of(createSoftSkillName(1)));

        // when
        var result = mockMvc.perform(post(JOB_OFFER_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jobOfferToCreate)));

        // then
        verify(jobOfferMapper, times(1)).fromWriteDto(jobOfferToCreate);
        verify(jobOfferService, times(1)).create(any(), any(), any(), any(), any(), any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingJobOfferException.getMessage()));
    }

    @Test
    void shouldReturnModifiedJobOffer() throws Exception {
        // given
        var jobOfferId = 1;
        var jobOfferToModify = createJobOfferWriteDto(1);
        var modifiedJobOffer = createJobOfferReadDto(1);

        when(jobOfferMapper.toReadDto(any())).thenReturn(modifiedJobOffer);
        when(techSkillNameService.getByName(any())).thenReturn(Optional.of(createTechSkillName(1)));
        when(softSkillNameService.getByName(any())).thenReturn(Optional.of(createSoftSkillName(1)));

        // when
        var result = mockMvc.perform(put(JOB_OFFER_API_URL + "/" + jobOfferId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jobOfferToModify)));

        // then
        verify(jobOfferMapper, times(1)).fromWriteDto(any());
        verify(jobOfferService, times(1)).update(any(), any(), any(), any(), any(), any(), any());
        verify(jobOfferMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedJobOffer.id()))
                .andExpect(jsonPath("$.name").value(modifiedJobOffer.name()))
                .andExpect(jsonPath("$.position").value(modifiedJobOffer.position()));
    }

    @Test
    void shouldReturnErrorWhenEditedJobOfferNotFound() throws Exception {
        // given
        var jobOfferId = 1;
        var jobOfferToModify = createJobOfferWriteDto(1);
        var jobOfferNotFoundException = new EntityNotFoundException(jobOfferId);

        when(jobOfferService.update(any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(jobOfferNotFoundException);
        when(techSkillNameService.getByName(any())).thenReturn(Optional.of(createTechSkillName(1)));
        when(softSkillNameService.getByName(any())).thenReturn(Optional.of(createSoftSkillName(1)));

        // when
        var result = mockMvc.perform(put(JOB_OFFER_API_URL + "/" + jobOfferId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(jobOfferToModify)));

        // then
        verify(jobOfferMapper, times(1)).fromWriteDto(any());
        verify(jobOfferService, times(1)).update(any(), any(), any(), any(), any(), any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(jobOfferNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteJobOffer() throws Exception {
        // given
        var jobOfferId = 1;

        // when
        var result = mockMvc.perform(delete(JOB_OFFER_API_URL + "/" + jobOfferId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(jobOfferService, times(1)).delete(jobOfferId);

        result.andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(getDeletedEntityResponse(jobOfferId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedJobOfferNotFound() throws Exception {
        // given
        var jobOfferId = 1;
        var jobOfferNotFoundException = new EntityNotFoundException(jobOfferId);

        doThrow(jobOfferNotFoundException).when(jobOfferService).delete(jobOfferId);

        // when
        var result = mockMvc.perform(delete(JOB_OFFER_API_URL + "/" + jobOfferId)
                .with(createMockJwt("auth0Id1")));

        // then
        verify(jobOfferService, times(1)).delete(jobOfferId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(jobOfferNotFoundException.getMessage()));
    }
}
