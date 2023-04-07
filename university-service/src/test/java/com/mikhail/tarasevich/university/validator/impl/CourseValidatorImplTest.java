package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.CourseValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseValidatorImplTest {

    @InjectMocks
    CourseValidatorImpl courseValidator;
    @Mock
    CourseDao courseDao;

    private static final CourseRequest COURSE_REQUEST = new CourseRequest();

    static {
        COURSE_REQUEST.setId(1);
        COURSE_REQUEST.setName("course");
        COURSE_REQUEST.setDescription("description");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(courseDao.findByName(COURSE_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> courseValidator.validateUniqueNameInDB(COURSE_REQUEST));

        verify(courseDao, times(1)).findByName(COURSE_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(courseDao.findByName(COURSE_REQUEST.getName())).thenReturn(Optional.of(Course.builder().build()));

        assertThrows(IncorrectRequestDataException.class, () -> courseValidator.validateUniqueNameInDB(COURSE_REQUEST));

        verify(courseDao, times(1)).findByName(COURSE_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> courseValidator.validateNameNotNullOrEmpty(COURSE_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final CourseRequest COURSE_REQUEST = new CourseRequest();
        COURSE_REQUEST.setId(1);
        COURSE_REQUEST.setName("  ");
        COURSE_REQUEST.setDescription("description");

        assertThrows(IncorrectRequestDataException.class, () -> courseValidator.validateNameNotNullOrEmpty(COURSE_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final CourseRequest COURSE_REQUEST = new CourseRequest();
        COURSE_REQUEST.setId(1);
        COURSE_REQUEST.setName(null);
        COURSE_REQUEST.setDescription("description");

        assertThrows(IncorrectRequestDataException.class, () -> courseValidator.validateNameNotNullOrEmpty(COURSE_REQUEST));
    }

}
