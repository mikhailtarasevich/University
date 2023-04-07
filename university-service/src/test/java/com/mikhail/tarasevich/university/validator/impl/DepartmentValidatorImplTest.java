package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.DepartmentValidatorImpl;
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
class DepartmentValidatorImplTest {

    @InjectMocks
    DepartmentValidatorImpl departmentValidator;
    @Mock
    DepartmentDao departmentDao;

    private static final DepartmentRequest DEPARTMENT_REQUEST = new DepartmentRequest();

    static {
        DEPARTMENT_REQUEST.setId(1);
        DEPARTMENT_REQUEST.setName("name");
        DEPARTMENT_REQUEST.setDescription("description");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(departmentDao.findByName(DEPARTMENT_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> departmentValidator.validateUniqueNameInDB(DEPARTMENT_REQUEST));

        verify(departmentDao, times(1)).findByName(DEPARTMENT_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(departmentDao.findByName(DEPARTMENT_REQUEST.getName()))
                .thenReturn(Optional.of(Department.builder().build()));

        assertThrows(IncorrectRequestDataException.class, () -> departmentValidator.validateUniqueNameInDB(DEPARTMENT_REQUEST));

        verify(departmentDao, times(1)).findByName(DEPARTMENT_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> departmentValidator.validateNameNotNullOrEmpty(DEPARTMENT_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final DepartmentRequest DEPARTMENT_REQUEST = new DepartmentRequest();
        DEPARTMENT_REQUEST.setId(1);
        DEPARTMENT_REQUEST.setName("   ");
        DEPARTMENT_REQUEST.setDescription("description");

        assertThrows(IncorrectRequestDataException.class,
                () -> departmentValidator.validateNameNotNullOrEmpty(DEPARTMENT_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final DepartmentRequest DEPARTMENT_REQUEST = new DepartmentRequest();
        DEPARTMENT_REQUEST.setId(1);
        DEPARTMENT_REQUEST.setName(null);
        DEPARTMENT_REQUEST.setDescription("description");

        assertThrows(IncorrectRequestDataException.class,
                () -> departmentValidator.validateNameNotNullOrEmpty(DEPARTMENT_REQUEST));
    }

}
