package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.repository.TeacherTitleRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.TeacherTitleValidatorImpl;
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
class TeacherTitleValidatorImplTest {

    @InjectMocks
    TeacherTitleValidatorImpl teacherTitleValidator;
    @Mock
    TeacherTitleRepository teacherTitleRepository;

    private static final TeacherTitleRequest TEACHER_TITLE_REQUEST = new TeacherTitleRequest();

    static {
        TEACHER_TITLE_REQUEST.setId(1);
        TEACHER_TITLE_REQUEST.setName("name1");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(teacherTitleRepository.findByName(TEACHER_TITLE_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> teacherTitleValidator.validateUniqueNameInDB(TEACHER_TITLE_REQUEST));

        verify(teacherTitleRepository, times(1)).findByName(TEACHER_TITLE_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(teacherTitleRepository.findByName(TEACHER_TITLE_REQUEST.getName()))
                .thenReturn(Optional.of(TeacherTitle.builder().build()));

        assertThrows(IncorrectRequestDataException.class,
                () -> teacherTitleValidator.validateUniqueNameInDB(TEACHER_TITLE_REQUEST));

        verify(teacherTitleRepository, times(1)).findByName(TEACHER_TITLE_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> teacherTitleValidator.validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final TeacherTitleRequest TEACHER_TITLE_REQUEST = new TeacherTitleRequest();
        TEACHER_TITLE_REQUEST.setId(1);
        TEACHER_TITLE_REQUEST.setName("  ");

        assertThrows(IncorrectRequestDataException.class,
                () -> teacherTitleValidator.validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final TeacherTitleRequest TEACHER_TITLE_REQUEST = new TeacherTitleRequest();
        TEACHER_TITLE_REQUEST.setId(1);
        TEACHER_TITLE_REQUEST.setName(null);

        assertThrows(IncorrectRequestDataException.class,
                () -> teacherTitleValidator.validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST));
    }

}
