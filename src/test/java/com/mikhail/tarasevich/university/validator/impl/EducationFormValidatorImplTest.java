package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.entity.EducationForm;
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
class EducationFormValidatorImplTest {

    @InjectMocks
    EducationFormValidatorImpl educationFormValidator;
    @Mock
    EducationFormDao educationFormDao;

    private static final EducationFormRequest EDUCATION_FORM_REQUEST = new EducationFormRequest();

    static {
        EDUCATION_FORM_REQUEST.setId(1);
        EDUCATION_FORM_REQUEST.setName("name");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(educationFormDao.findByName(EDUCATION_FORM_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> educationFormValidator.validateUniqueNameInDB(EDUCATION_FORM_REQUEST));

        verify(educationFormDao, times(1)).findByName(EDUCATION_FORM_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(educationFormDao.findByName(EDUCATION_FORM_REQUEST.getName()))
                .thenReturn(Optional.of(EducationForm.builder().build()));

        assertThrows(IncorrectRequestData.class,
                () -> educationFormValidator.validateUniqueNameInDB(EDUCATION_FORM_REQUEST));

        verify(educationFormDao, times(1)).findByName(EDUCATION_FORM_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> educationFormValidator.validateNameNotNullOrEmpty(EDUCATION_FORM_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        EducationFormRequest EDUCATION_FORM_REQUEST = new EducationFormRequest();
        EDUCATION_FORM_REQUEST.setId(1);
        EDUCATION_FORM_REQUEST.setName("   ");

        assertThrows(IncorrectRequestData.class,
                () -> educationFormValidator.validateNameNotNullOrEmpty(EDUCATION_FORM_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        EducationFormRequest EDUCATION_FORM_REQUEST = new EducationFormRequest();
        EDUCATION_FORM_REQUEST.setId(1);
        EDUCATION_FORM_REQUEST.setName(null);

        assertThrows(IncorrectRequestData.class,
                () -> educationFormValidator.validateNameNotNullOrEmpty(EDUCATION_FORM_REQUEST));
    }

}
