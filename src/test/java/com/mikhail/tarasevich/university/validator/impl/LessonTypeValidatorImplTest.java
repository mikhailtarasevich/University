package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
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
class LessonTypeValidatorImplTest {

    @InjectMocks
    LessonTypeValidatorImpl lessonTypeValidator;
    @Mock
    LessonTypeDao lessonTypeDao;

    private static final LessonTypeRequest LESSON_TYPE_REQUEST = new LessonTypeRequest();

    static {
        LESSON_TYPE_REQUEST.setId(1);
        LESSON_TYPE_REQUEST.setName("name1");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(lessonTypeDao.findByName(LESSON_TYPE_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> lessonTypeValidator.validateUniqueNameInDB(LESSON_TYPE_REQUEST));

        verify(lessonTypeDao, times(1)).findByName(LESSON_TYPE_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(lessonTypeDao.findByName(LESSON_TYPE_REQUEST.getName()))
                .thenReturn(Optional.of(LessonType.builder().build()));

        assertThrows(IncorrectRequestDataException.class, () -> lessonTypeValidator.validateUniqueNameInDB(LESSON_TYPE_REQUEST));

        verify(lessonTypeDao, times(1)).findByName(LESSON_TYPE_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> lessonTypeValidator.validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final LessonTypeRequest LESSON_TYPE_REQUEST = new LessonTypeRequest();
        LESSON_TYPE_REQUEST.setId(1);
        LESSON_TYPE_REQUEST.setName("   ");

        assertThrows(IncorrectRequestDataException.class,
                () -> lessonTypeValidator.validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final LessonTypeRequest LESSON_TYPE_REQUEST = new LessonTypeRequest();
        LESSON_TYPE_REQUEST.setId(1);
        LESSON_TYPE_REQUEST.setName(null);

        assertThrows(IncorrectRequestDataException.class,
                () -> lessonTypeValidator.validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST));
    }

}
