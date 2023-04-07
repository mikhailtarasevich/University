package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.LessonValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonValidatorImplTest {

    @InjectMocks
    LessonValidatorImpl lessonValidator;
    @Mock
    LessonDao lessonDao;

    private static final LessonRequest LESSON_REQUEST = new LessonRequest();

    static {
        LESSON_REQUEST.setId(1);
        LESSON_REQUEST.setName("name1");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(lessonDao.findByName(LESSON_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> lessonValidator.validateUniqueNameInDB(LESSON_REQUEST));

        verify(lessonDao, times(1)).findByName(LESSON_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(lessonDao.findByName(LESSON_REQUEST.getName())).thenReturn(Optional.of(Lesson.builder().build()));

        assertThrows(IncorrectRequestDataException.class, () -> lessonValidator.validateUniqueNameInDB(LESSON_REQUEST));

        verify(lessonDao, times(1)).findByName(LESSON_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> lessonValidator.validateNameNotNullOrEmpty(LESSON_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final LessonRequest LESSON_REQUEST = new LessonRequest();
        LESSON_REQUEST.setId(1);
        LESSON_REQUEST.setName("  ");

        assertThrows(IncorrectRequestDataException.class, () -> lessonValidator.validateNameNotNullOrEmpty(LESSON_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final LessonRequest LESSON_REQUEST = new LessonRequest();
        LESSON_REQUEST.setId(1);
        LESSON_REQUEST.setName(null);

        assertThrows(IncorrectRequestDataException.class, () -> lessonValidator.validateNameNotNullOrEmpty(LESSON_REQUEST));
    }

    @Test
    void validateStartTimeNotNull_inputStartTimeNotNull_expectedNothing() {
        final LessonRequest LESSON_REQUEST = new LessonRequest();
        LESSON_REQUEST.setStartTime(LocalDateTime.now());
        assertDoesNotThrow(() -> lessonValidator.validateStartTimeNotNull(LESSON_REQUEST));
    }

    @Test
    void validateStartTimeNotNull_inputStartTimeIsNull_expectedNothing() {
        assertThrows(IncorrectRequestDataException.class, () -> lessonValidator.validateStartTimeNotNull(LESSON_REQUEST));
    }

}
