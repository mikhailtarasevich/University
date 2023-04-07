package com.mikhail.tarasevich.university.validator.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.validator.impl.GroupValidatorImpl;
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

    private static final GroupRequest GROUP_REQUEST = new GroupRequest();

    static {
        GROUP_REQUEST.setId(1);
        GROUP_REQUEST.setName("groupName");
    }

    @Test
    void validateUniqueNameInDB_inputWithUniqueName_expectedNothing() {
        when(groupDao.findByName(GROUP_REQUEST.getName())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> groupValidator.validateUniqueNameInDB(GROUP_REQUEST));

        verify(groupDao, times(1)).findByName(GROUP_REQUEST.getName());
    }

    @Test
    void validateUniqueNameInDB_inputWithNotUniqueName_expectedException() {
        when(groupDao.findByName(GROUP_REQUEST.getName())).thenReturn(Optional.of(Group.builder().build()));

        assertThrows(IncorrectRequestDataException.class, () -> groupValidator.validateUniqueNameInDB(GROUP_REQUEST));

        verify(groupDao, times(1)).findByName(GROUP_REQUEST.getName());
    }

    @Test
    void validateNameNotNullOrEmpty_inputCorrectName_expectedNothing() {
        assertDoesNotThrow(() -> groupValidator.validateNameNotNullOrEmpty(GROUP_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputEmptyName_expectedException() {
        final GroupRequest GROUP_REQUEST = new GroupRequest();
        GROUP_REQUEST.setId(1);
        GROUP_REQUEST.setName("  ");

        assertThrows(IncorrectRequestDataException.class, () -> groupValidator.validateNameNotNullOrEmpty(GROUP_REQUEST));
    }

    @Test
    void validateNameNotNullOrEmpty_inputNullName_expectedException() {
        final GroupRequest GROUP_REQUEST = new GroupRequest();
        GROUP_REQUEST.setId(1);
        GROUP_REQUEST.setName(null);

        assertThrows(IncorrectRequestDataException.class, () -> groupValidator.validateNameNotNullOrEmpty(GROUP_REQUEST));
    }

}
