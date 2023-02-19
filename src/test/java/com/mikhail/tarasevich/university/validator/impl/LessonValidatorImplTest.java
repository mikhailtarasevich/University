package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dto.LessonRequest;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private static final LessonRequest LESSON_REQUEST =
            new LessonRequest(1, "name1", null, null, null, null, null);

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(lessonDao.findByName(LESSON_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> lessonValidator.validateUniqueNameInDB(LESSON_REQUEST));

        verify(lessonDao, times(1)).findByName(LESSON_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(lessonDao.findByName(LESSON_REQUEST.getName())).thenReturn(Optional.of(Lesson.builder().build()));

        assertThrows(IncorrectRequestData.class, () -> lessonValidator.validateUniqueNameInDB(LESSON_REQUEST));

        verify(lessonDao, times(1)).findByName(LESSON_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> lessonValidator.validateNameNotNullOrEmpty(LESSON_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final LessonRequest LESSON_REQUEST =
                new LessonRequest(1, "  ", null, null, null, null, null);

        assertThrows(IncorrectRequestData.class, () -> lessonValidator.validateNameNotNullOrEmpty(LESSON_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final LessonRequest LESSON_REQUEST =
                new LessonRequest(1, null, null, null, null, null, null);

        assertThrows(IncorrectRequestData.class, () -> lessonValidator.validateNameNotNullOrEmpty(LESSON_REQUEST));
    }

}