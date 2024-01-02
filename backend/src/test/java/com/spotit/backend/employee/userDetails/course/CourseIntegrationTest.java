package com.spotit.backend.employee.userDetails.course;

import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.abstraction.GeneralUtils.getEntityNotFoundMessage;
import static com.spotit.backend.abstraction.GeneralUtils.getErrorCreatingEntityMessage;
import static com.spotit.backend.employee.userAccount.UserAccountUtils.createUserAccount;
import static com.spotit.backend.employee.userDetails.course.CourseUtils.createCourse;
import static com.spotit.backend.employee.userDetails.course.CourseUtils.createCourseWriteDto;
import static com.spotit.backend.employee.userDetails.course.CourseUtils.generateCourseList;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.IntegrationTest;
import com.spotit.backend.employee.userAccount.UserAccount;
import com.spotit.backend.employee.userAccount.UserAccountRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class CourseIntegrationTest extends IntegrationTest {

    static final String COURSE_API_URL = "/api/userAccount/auth0Id1/course";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    CourseRepository courseRepository;

    UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = createUserAccount(1);
        userAccountRepository.save(userAccount);
    }

    @Test
    void shouldReturnListOfUserCourses() throws Exception {
        // given
        var coursestoCreate = generateCourseList(3);
        coursestoCreate.stream().forEach(social -> social.setUserAccount(userAccount));
        courseRepository.saveAll(coursestoCreate);

        // when
        var result = mockMvc.perform(get(COURSE_API_URL).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCourseById() throws Exception {
        // given
        var coursestoCreate = generateCourseList(3);
        coursestoCreate.stream().forEach(social -> social.setUserAccount(userAccount));
        var createdCourses = courseRepository.saveAll(coursestoCreate);
        var wantedCourse = createdCourses.get(1);

        // when
        var result = mockMvc.perform(get(COURSE_API_URL + "/" + wantedCourse.getId()).with(jwt()));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(wantedCourse.getId()))
                .andExpect(jsonPath("$.name").value(wantedCourse.getName()))
                .andExpect(jsonPath("$.finishDate").value(wantedCourse.getFinishDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCourseNotFound() throws Exception {
        // given
        var courseId = 1;

        // when
        var result = mockMvc.perform(get(COURSE_API_URL + "/" + courseId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(courseId)));
    }

    @Test
    void shouldReturnCreatedCourse() throws Exception {
        // given
        var courseToCreate = createCourseWriteDto(1);

        // when
        var result = mockMvc.perform(post(COURSE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToCreate)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(courseToCreate.name()))
                .andExpect(jsonPath("$.finishDate").value(courseToCreate.finishDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCreatingCourse() throws Exception {
        // given
        var courseWithBadDate = CourseWriteDto.builder()
                .name("Name")
                .finishDate(null)
                .build();

        // when
        var result = mockMvc.perform(post(COURSE_API_URL)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseWithBadDate)));

        // then
        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(getErrorCreatingEntityMessage()));
    }

    @Test
    void shouldReturnModifiedCourse() throws Exception {
        // given
        var courseToCreate = createCourse(1);
        courseToCreate.setUserAccount(userAccount);
        var existingCourse = courseRepository.save(courseToCreate);
        var modifiedCourse = createCourseWriteDto(2);

        // when
        var result = mockMvc.perform(put(COURSE_API_URL + "/" + existingCourse.getId())
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedCourse)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingCourse.getId()))
                .andExpect(jsonPath("$.name").value(modifiedCourse.name()))
                .andExpect(jsonPath("$.finishDate").value(modifiedCourse.finishDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEditedCourseNotFound() throws Exception {
        // given
        var courseId = 1;
        var modifiedCourse = createCourseWriteDto(2);

        // when
        var result = mockMvc.perform(put(COURSE_API_URL + "/" + courseId)
                .with(jwt())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(modifiedCourse)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(courseId)));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
        // given
        var courseToCreate = createCourse(1);
        courseToCreate.setUserAccount(userAccount);
        var courseToDelete = courseRepository.save(courseToCreate);

        // when
        var result = mockMvc.perform(delete(COURSE_API_URL + "/" + courseToDelete.getId()).with(jwt()));

        // then
        assertFalse(courseRepository.existsById(courseToDelete.getId()));

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(courseToDelete.getId()))));
    }

    @Test
    void shouldReturnErrorWhenDeletedNotFound() throws Exception {
        // given
        var courseId = 1;

        // when
        var result = mockMvc.perform(delete(COURSE_API_URL + "/" + courseId).with(jwt()));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(content().string(getEntityNotFoundMessage(courseId)));
    }
}
