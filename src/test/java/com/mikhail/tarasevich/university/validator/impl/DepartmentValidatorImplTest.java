package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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

    private static final DepartmentRequest DEPARTMENT_REQUEST = new DepartmentRequest(
            1, "name", "description", new ArrayList<>(), new ArrayList<>()
    );

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

        assertThrows(IncorrectRequestData.class, () -> departmentValidator.validateUniqueNameInDB(DEPARTMENT_REQUEST));

        verify(departmentDao, times(1)).findByName(DEPARTMENT_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> departmentValidator.validateNameNotNullOrEmpty(DEPARTMENT_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        DepartmentRequest DEPARTMENT_REQUEST = new DepartmentRequest(
                1, " ", "description", new ArrayList<>(), new ArrayList<>()
        );

        assertThrows(IncorrectRequestData.class,
                () -> departmentValidator.validateNameNotNullOrEmpty(DEPARTMENT_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final DepartmentRequest DEPARTMENT_REQUEST = new DepartmentRequest(
                1, null, "description", new ArrayList<>(), new ArrayList<>()
        );

        assertThrows(IncorrectRequestData.class,
                () -> departmentValidator.validateNameNotNullOrEmpty(DEPARTMENT_REQUEST));
    }

}
