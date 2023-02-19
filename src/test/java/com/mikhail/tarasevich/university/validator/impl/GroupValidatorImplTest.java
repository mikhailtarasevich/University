package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.entity.Group;
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
class GroupValidatorImplTest {

    @InjectMocks
    GroupValidatorImpl groupValidator;
    @Mock
    GroupDao groupDao;

    private static final GroupRequest groupRequest = new GroupRequest(0, "name", null, null, null);

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(groupDao.findByName(groupRequest.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> groupValidator.validateUniqueNameInDB(groupRequest));

        verify(groupDao, times(1)).findByName(groupRequest.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(groupDao.findByName(groupRequest.getName())).thenReturn(Optional.of(Group.builder().build()));

        assertThrows(IncorrectRequestData.class, () -> groupValidator.validateUniqueNameInDB(groupRequest));

        verify(groupDao, times(1)).findByName(groupRequest.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> groupValidator.validateNameNotNullOrEmpty(groupRequest));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final GroupRequest groupRequest =
                new GroupRequest(0, "  ", null, null, null);

        assertThrows(IncorrectRequestData.class, () -> groupValidator.validateNameNotNullOrEmpty(groupRequest));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final GroupRequest groupRequest =
                new GroupRequest(0, null, null, null, null);

        assertThrows(IncorrectRequestData.class, () -> groupValidator.validateNameNotNullOrEmpty(groupRequest));
    }

}
