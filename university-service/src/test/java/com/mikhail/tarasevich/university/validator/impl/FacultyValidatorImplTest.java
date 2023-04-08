package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.FacultyValidatorImpl;
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
class FacultyValidatorImplTest {

    @InjectMocks
    FacultyValidatorImpl facultyValidator;
    @Mock
    FacultyDao facultyDao;

    private static final FacultyRequest FACULTY_REQUEST = new FacultyRequest();

    static {
        FACULTY_REQUEST.setId(1);
        FACULTY_REQUEST.setName("faculty");
        FACULTY_REQUEST.setDescription("description");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(facultyDao.findByName(FACULTY_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> facultyValidator.validateUniqueNameInDB(FACULTY_REQUEST));

        verify(facultyDao, times(1)).findByName(FACULTY_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(facultyDao.findByName(FACULTY_REQUEST.getName())).thenReturn(Optional.of(Faculty.builder().build()));

        assertThrows(IncorrectRequestDataException.class, () -> facultyValidator.validateUniqueNameInDB(FACULTY_REQUEST));

        verify(facultyDao, times(1)).findByName(FACULTY_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> facultyValidator.validateNameNotNullOrEmpty(FACULTY_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final FacultyRequest FACULTY_REQUEST = new FacultyRequest();
        FACULTY_REQUEST.setId(1);
        FACULTY_REQUEST.setName("    ");
        FACULTY_REQUEST.setDescription("description");

        assertThrows(IncorrectRequestDataException.class, () -> facultyValidator.validateNameNotNullOrEmpty(FACULTY_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final FacultyRequest FACULTY_REQUEST = new FacultyRequest();
        FACULTY_REQUEST.setId(1);
        FACULTY_REQUEST.setName(null);
        FACULTY_REQUEST.setDescription("description");

        assertThrows(IncorrectRequestDataException.class, () -> facultyValidator.validateNameNotNullOrEmpty(FACULTY_REQUEST));
    }

}
