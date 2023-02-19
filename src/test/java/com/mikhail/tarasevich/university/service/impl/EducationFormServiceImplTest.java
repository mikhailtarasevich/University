package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.EducationFormMapper;
import com.mikhail.tarasevich.university.validator.EducationFormValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class EducationFormServiceImplTest {

    @InjectMocks
    EducationFormServiceImpl educationFormService;
    @Mock
    EducationFormDao educationFormDao;
    @Mock
    GroupDao groupDao;
    @Mock
    EducationFormMapper educationFormMapper;
    @Mock
    EducationFormValidator educationFormValidator;

    private static final EducationForm EF_ENTITY_1 =
            EducationForm.builder().withName("name1").build();
    private static final EducationForm EF_ENTITY_WITH_ID_1 =
            EducationForm.builder().withId(1).withName("name1").build();
    private static final EducationFormRequest EF_REQUEST_1 =
            new EducationFormRequest(0, "name1");
    private static final EducationFormResponse EF_RESPONSE_WITH_ID_1 =
            new EducationFormResponse(1, "name1");

    private static final EducationForm EF_ENTITY_2 =
            EducationForm.builder().withName("name2").build();
    private static final EducationForm EF_ENTITY_WITH_ID_2 =
            EducationForm.builder().withId(2).withName("name2").build();
    private static final EducationFormRequest EF_REQUEST_2 =
            new EducationFormRequest(0, "name2");
    private static final EducationFormResponse EF_RESPONSE_WITH_ID_2 =
            new EducationFormResponse(2, "name2");

    private final List<EducationForm> educationFormEntities = new ArrayList<>();
    private final List<EducationForm> educationFormEntitiesWithId = new ArrayList<>();
    private final List<EducationFormResponse> educationFormResponses = new ArrayList<>();

    {
        educationFormEntities.add(EF_ENTITY_1);
        educationFormEntities.add(EF_ENTITY_2);

        educationFormEntitiesWithId.add(EF_ENTITY_WITH_ID_1);
        educationFormEntitiesWithId.add(EF_ENTITY_WITH_ID_2);

        educationFormResponses.add(EF_RESPONSE_WITH_ID_1);
        educationFormResponses.add(EF_RESPONSE_WITH_ID_2);
    }


    @Test
    void register_inputEducationFormRequest_expectedEducationFormResponseWithId() {
        when(educationFormMapper.toEntity(EF_REQUEST_1)).thenReturn(EF_ENTITY_1);
        when(educationFormDao.save(EF_ENTITY_1)).thenReturn(EF_ENTITY_WITH_ID_1);
        when(educationFormMapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);
        doNothing().when(educationFormValidator).validateUniqueNameInDB(EF_REQUEST_1);
        doNothing().when(educationFormValidator).validateNameNotNullOrEmpty(EF_REQUEST_1);

        EducationFormResponse educationFormResponse = educationFormService.register(EF_REQUEST_1);

        assertEquals(EF_RESPONSE_WITH_ID_1, educationFormResponse);
        verify(educationFormMapper, times(1)).toEntity(EF_REQUEST_1);
        verify(educationFormDao, times(1)).save(EF_ENTITY_1);
        verify(educationFormMapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(educationFormValidator, times(1)).validateUniqueNameInDB(EF_REQUEST_1);
        verify(educationFormValidator, times(1)).validateUniqueNameInDB(EF_REQUEST_1);
    }

    @Test
    void registerAllEducationForms_inputEducationFormRequestListWithRepeatableEducationForm_expectedNothing() {
        final List<EducationFormRequest> listForRegister = new ArrayList<>();
        listForRegister.add(EF_REQUEST_1);
        listForRegister.add(EF_REQUEST_1);
        listForRegister.add(EF_REQUEST_2);

        when(educationFormMapper.toEntity(EF_REQUEST_1)).thenReturn(EF_ENTITY_1);
        when(educationFormMapper.toEntity(EF_REQUEST_2)).thenReturn(EF_ENTITY_2);
        doNothing().when(educationFormDao).saveAll(educationFormEntities);
        doNothing().doThrow(new IncorrectRequestData()).when(educationFormValidator).validateUniqueNameInDB(EF_REQUEST_1);
        doNothing().when(educationFormValidator).validateNameNotNullOrEmpty(EF_REQUEST_1);
        doNothing().when(educationFormValidator).validateUniqueNameInDB(EF_REQUEST_2);
        doNothing().when(educationFormValidator).validateNameNotNullOrEmpty(EF_REQUEST_2);

        educationFormService.registerAll(listForRegister);

        verify(educationFormMapper, times(1)).toEntity(EF_REQUEST_2);
        verify(educationFormMapper, times(1)).toEntity(EF_REQUEST_2);
        verify(educationFormDao, times(1)).saveAll(educationFormEntities);
        verify(educationFormValidator, times(2)).validateUniqueNameInDB(EF_REQUEST_1);
        verify(educationFormValidator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_1);
        verify(educationFormValidator, times(1)).validateUniqueNameInDB(EF_REQUEST_2);
        verify(educationFormValidator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundEducationForm() {
        when(educationFormDao.findById(1)).thenReturn(Optional.of(EF_ENTITY_WITH_ID_1));
        when(educationFormMapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);

        Optional<EducationFormResponse> educationFormResponse = educationFormService.findById(1);

        assertEquals(Optional.of(EF_RESPONSE_WITH_ID_1), educationFormResponse);
        verify(educationFormMapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(educationFormDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundEducationFormsFromPageOne() {
        when(educationFormDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(educationFormEntitiesWithId);
        when(educationFormDao.count()).thenReturn(2L);
        when(educationFormMapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);
        when(educationFormMapper.toResponse(EF_ENTITY_WITH_ID_2)).thenReturn(EF_RESPONSE_WITH_ID_2);

        List<EducationFormResponse> foundCourses = educationFormService.findAll("1");

        assertEquals(educationFormResponses, foundCourses);
        verify(educationFormDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(educationFormDao, times(1)).count();
        verify(educationFormMapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(educationFormMapper, times(1)).toResponse(EF_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputEducationFormRequest_expectedNothing() {
        final EducationForm EF_ENTITY_FOR_UPDATE_1 =
                EducationForm.builder().withId(1).withName("update1").build();
        final EducationFormRequest EF_REQUEST_FOR_UPDATE_1 =
                new EducationFormRequest(1, "update1");

        doNothing().when(educationFormDao).update(EF_ENTITY_FOR_UPDATE_1);
        when(educationFormMapper.toEntity(EF_REQUEST_FOR_UPDATE_1)).thenReturn(EF_ENTITY_FOR_UPDATE_1);

        educationFormService.edit(EF_REQUEST_FOR_UPDATE_1);

        verify(educationFormMapper, times(1)).toEntity(EF_REQUEST_FOR_UPDATE_1);
        verify(educationFormDao, times(1)).update(EF_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputEducationFormRequestListWhereOneEducationFormHasIncorrectName_expectedNothing() {
        final EducationForm EF_ENTITY_FOR_UPDATE_1 =
                EducationForm.builder().withId(1).withName("update1").build();
        final EducationFormRequest EF_REQUEST_FOR_UPDATE_1 =
                new EducationFormRequest(1, "update1");
        final EducationForm EF_ENTITY_FOR_UPDATE_2 =
                EducationForm.builder().withId(2).withName("update2").build();
        final EducationFormRequest EF_REQUEST_FOR_UPDATE_2 =
                new EducationFormRequest(2, "update2");
        final EducationFormRequest EF_REQUEST_FOR_UPDATE_INCORRECT =
                new EducationFormRequest(3, " ");

        final List<EducationFormRequest> inputList = new ArrayList<>();
        inputList.add(EF_REQUEST_FOR_UPDATE_1);
        inputList.add(EF_REQUEST_FOR_UPDATE_2);
        inputList.add(EF_REQUEST_FOR_UPDATE_INCORRECT);

        final List<EducationForm> listForUpdate = new ArrayList<>();
        listForUpdate.add(EF_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(EF_ENTITY_FOR_UPDATE_2);

        doNothing().when(educationFormValidator).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_1);
        doNothing().when(educationFormValidator).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData()).when(educationFormValidator).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_INCORRECT);

        when(educationFormMapper.toEntity(EF_REQUEST_FOR_UPDATE_1)).thenReturn(EF_ENTITY_FOR_UPDATE_1);
        when(educationFormMapper.toEntity(EF_REQUEST_FOR_UPDATE_2)).thenReturn(EF_ENTITY_FOR_UPDATE_2);
        doNothing().when(educationFormDao).updateAll(listForUpdate);

        educationFormService.editAll(inputList);

        verify(educationFormMapper, times(1)).toEntity(EF_REQUEST_FOR_UPDATE_1);
        verify(educationFormMapper, times(1)).toEntity(EF_REQUEST_FOR_UPDATE_2);
        verify(educationFormDao, times(1)).updateAll(listForUpdate);
        verify(educationFormValidator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_1);
        verify(educationFormValidator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_2);
        verify(educationFormValidator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputEducationFormId_expectedSuccessDelete() {
        int id = 1;

        when(educationFormDao.findById(id)).thenReturn(Optional.of(EF_ENTITY_WITH_ID_1));
        doNothing().when(groupDao).unbindGroupsFromEducationForm(id);
        when(educationFormDao.deleteById(id)).thenReturn(true);

        boolean result = educationFormService.deleteById(1);

        assertTrue(result);
        verify(educationFormDao, times(1)).findById(id);
        verify(educationFormDao, times(1)).deleteById(id);
        verify(groupDao, times(1)).unbindGroupsFromEducationForm(id);
    }

    @Test
    void deleteById_inputEducationFormId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(educationFormDao.findById(id)).thenReturn(Optional.empty());

        boolean result = educationFormService.deleteById(1);

        assertFalse(result);
        verify(educationFormDao, times(1)).findById(id);
        verifyNoInteractions(groupDao);
        verify(educationFormDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputEducationFormIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupDao).unbindGroupsFromEducationForm(1);
        doNothing().when(groupDao).unbindGroupsFromEducationForm(2);
        when(educationFormDao.deleteByIds(ids)).thenReturn(true);

        boolean result = educationFormService.deleteByIds(ids);

        assertTrue(result);
        verify(educationFormDao, times(1)).deleteByIds(ids);
        verify(groupDao, times(1)).unbindGroupsFromEducationForm(1);
        verify(groupDao, times(1)).unbindGroupsFromEducationForm(2);
    }

}
