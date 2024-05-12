package com.spotit.backend.domain.employee.employeeDetails.course;

import static com.spotit.backend.abstraction.GeneralUtils.createMockJwt;
import static com.spotit.backend.abstraction.GeneralUtils.getDeletedEntityResponse;
import static com.spotit.backend.domain.employee.employeeDetails.course.CourseUtils.createCourseReadDto;
import static com.spotit.backend.domain.employee.employeeDetails.course.CourseUtils.createCourseWriteDto;
import static com.spotit.backend.domain.employee.employeeDetails.course.CourseUtils.generateCourseList;
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

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotit.backend.abstraction.EntityNotFoundException;
import com.spotit.backend.abstraction.ErrorCreatingEntityException;
import com.spotit.backend.config.security.SecurityConfig;

@WebMvcTest(CourseController.class)
@Import(SecurityConfig.class)
class CourseControllerTest {

    static final String COURSE_API_URL = "/api/userAccount/auth0Id1/course";

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CourseService courseService;

    @MockBean
    CourseMapper courseMapper;

    @Test
    void shouldReturnListOfUserCourses() throws Exception {
        // given
        var userAuth0Id = "auth0Id1";

        when(courseService.getAllByUserAccountAuth0Id(userAuth0Id))
                .thenReturn(generateCourseList(3));
        when(courseMapper.toReadDto(any()))
                .thenReturn(createCourseReadDto(1));

        // when
        var result = mockMvc.perform(get(COURSE_API_URL).with(createMockJwt(userAuth0Id)));

        // then
        verify(courseService, times(1)).getAllByUserAccountAuth0Id(userAuth0Id);
        verify(courseMapper, times(3)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldReturnCreatedCourse() throws Exception {
        // given
        var courseToCreate = createCourseWriteDto(1);
        var createdCourse = createCourseReadDto(1);

        when(courseMapper.toReadDto(any()))
                .thenReturn(createdCourse);

        // when
        var result = mockMvc.perform(post(COURSE_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToCreate)));

        // then
        verify(courseMapper, times(1)).fromWriteDto(any());
        verify(courseService, times(1)).create(any(), any());
        verify(courseMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdCourse.id()))
                .andExpect(jsonPath("$.name").value(createdCourse.name()))
                .andExpect(jsonPath("$.finishDate").value(createdCourse.finishDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCantCreateCourse() throws Exception {
        // given
        var courseToCreate = createCourseWriteDto(1);
        var creatingCourseException = new ErrorCreatingEntityException();

        when(courseService.create(any(), any()))
                .thenThrow(creatingCourseException);

        // when
        var result = mockMvc.perform(post(COURSE_API_URL)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToCreate)));

        // then
        verify(courseMapper, times(1)).fromWriteDto(courseToCreate);
        verify(courseService, times(1)).create(any(), any());

        result.andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(creatingCourseException.getMessage()));
    }

    @Test
    void shouldReturnCourseById() throws Exception {
        // given
        var courseId = 1;
        var courseToFind = createCourseReadDto(1);

        when(courseMapper.toReadDto(any()))
                .thenReturn(courseToFind);

        // when
        var result = mockMvc.perform(get(COURSE_API_URL + "/" + courseId).with(createMockJwt("auth0Id1")));

        // then
        verify(courseService, times(1)).getById(courseId);
        verify(courseMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseToFind.id()))
                .andExpect(jsonPath("$.name").value(courseToFind.name()))
                .andExpect(jsonPath("$.finishDate").value(courseToFind.finishDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenCourseNotFound() throws Exception {
        // given
        var courseId = 1;
        var courseNotFoundException = new EntityNotFoundException(courseId);

        when(courseService.getById(courseId))
                .thenThrow(courseNotFoundException);

        // when
        var result = mockMvc.perform(get(COURSE_API_URL + "/" + courseId).with(createMockJwt("auth0Id1")));

        // then
        verify(courseService, times(1)).getById(courseId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(courseNotFoundException.getMessage()));
    }

    @Test
    void shouldReturnModifiedCourse() throws Exception {
        // given
        var courseId = 1;
        var courseToModify = createCourseWriteDto(1);
        var modifiedCourse = createCourseReadDto(1);

        when(courseMapper.toReadDto(any())).thenReturn(modifiedCourse);

        // when
        var result = mockMvc.perform(put(COURSE_API_URL + "/" + courseId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToModify)));

        // then
        verify(courseMapper, times(1)).fromWriteDto(any());
        verify(courseService, times(1)).update(any(), any());
        verify(courseMapper, times(1)).toReadDto(any());

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(modifiedCourse.id()))
                .andExpect(jsonPath("$.name").value(modifiedCourse.name()))
                .andExpect(jsonPath("$.finishDate").value(modifiedCourse.finishDate().toString()));
    }

    @Test
    void shouldReturnErrorWhenEditedCourseNotFound() throws Exception {
        // given
        var courseId = 1;
        var courseToModify = createCourseWriteDto(1);
        var courseNotFoundException = new EntityNotFoundException(courseId);

        when(courseService.update(any(), any()))
                .thenThrow(courseNotFoundException);

        // when
        var result = mockMvc.perform(put(COURSE_API_URL + "/" + courseId)
                .with(createMockJwt("auth0Id1"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(courseToModify)));

        // then
        verify(courseMapper, times(1)).fromWriteDto(any());
        verify(courseService, times(1)).update(any(), any());

        result.andExpect(status().isNotFound())
                .andExpect(content().string(courseNotFoundException.getMessage()));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
        // given
        var courseId = 1;

        // when
        var result = mockMvc.perform(delete(COURSE_API_URL + "/" + courseId).with(createMockJwt("auth0Id1")));

        // then
        verify(courseService, times(1)).delete(courseId);

        result.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(
                        getDeletedEntityResponse(courseId))));
    }

    @Test
    void shouldReturnErrorWhenDeletedCourseNotFound() throws Exception {
        // given
        var courseId = 1;
        var courseNotFoundException = new EntityNotFoundException(courseId);

        doThrow(courseNotFoundException).when(courseService).delete(courseId);

        // when
        var result = mockMvc.perform(delete(COURSE_API_URL + "/" + courseId).with(createMockJwt("auth0Id1")));

        // then
        verify(courseService, times(1)).delete(courseId);

        result.andExpect(status().isNotFound())
                .andExpect(content().string(courseNotFoundException.getMessage()));
    }
}
