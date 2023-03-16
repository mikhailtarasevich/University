package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dto.EducationFormRequest;
import com.mikhail.tarasevich.university.dto.EducationFormResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.exception.ObjectWithSpecifiedIdNotFoundException;
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
    EducationFormMapper mapper;
    @Mock
    EducationFormValidator validator;

    private static final EducationForm EF_ENTITY_1 = EducationForm.builder()
            .withName("name1")
            .build();
    private static final EducationForm EF_ENTITY_WITH_ID_1 = EducationForm.builder()
            .withId(1)
            .withName("name1")
            .build();
    private static final EducationFormRequest EF_REQUEST_1 = new EducationFormRequest();
    private static final EducationFormResponse EF_RESPONSE_WITH_ID_1 = new EducationFormResponse();

    private static final EducationForm EF_ENTITY_2 = EducationForm.builder()
            .withName("name2")
            .build();
    private static final EducationForm EF_ENTITY_WITH_ID_2 = EducationForm.builder()
            .withId(2)
            .withName("name2")
            .build();
    private static final EducationFormRequest EF_REQUEST_2 = new EducationFormRequest();
    private static final EducationFormResponse EF_RESPONSE_WITH_ID_2 = new EducationFormResponse();

    private static final List<EducationForm> educationFormEntities = new ArrayList<>();
    private static final List<EducationForm> educationFormEntitiesWithId = new ArrayList<>();
    private static final List<EducationFormResponse> educationFormResponses = new ArrayList<>();

    static {
        EF_REQUEST_1.setId(0);
        EF_REQUEST_1.setName("name1");

        EF_RESPONSE_WITH_ID_1.setId(1);
        EF_RESPONSE_WITH_ID_1.setName("name1");

        EF_REQUEST_2.setId(0);
        EF_REQUEST_2.setName("name2");

        EF_RESPONSE_WITH_ID_2.setId(2);
        EF_RESPONSE_WITH_ID_2.setName("name2");

        educationFormEntities.add(EF_ENTITY_1);
        educationFormEntities.add(EF_ENTITY_2);

        educationFormEntitiesWithId.add(EF_ENTITY_WITH_ID_1);
        educationFormEntitiesWithId.add(EF_ENTITY_WITH_ID_2);

        educationFormResponses.add(EF_RESPONSE_WITH_ID_1);
        educationFormResponses.add(EF_RESPONSE_WITH_ID_2);
    }


    @Test
    void register_inputEducationFormRequest_expectedEducationFormResponseWithId() {
        when(mapper.toEntity(EF_REQUEST_1)).thenReturn(EF_ENTITY_1);
        when(educationFormDao.save(EF_ENTITY_1)).thenReturn(EF_ENTITY_WITH_ID_1);
        when(mapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(EF_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(EF_REQUEST_1);

        EducationFormResponse educationFormResponse = educationFormService.register(EF_REQUEST_1);

        assertEquals(EF_RESPONSE_WITH_ID_1, educationFormResponse);
        verify(mapper, times(1)).toEntity(EF_REQUEST_1);
        verify(educationFormDao, times(1)).save(EF_ENTITY_1);
        verify(mapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(EF_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(EF_REQUEST_1);
    }

    @Test
    void registerAllEducationForms_inputEducationFormRequestListWithRepeatableEducationForm_expectedNothing() {
        final List<EducationFormRequest> listForRegister = new ArrayList<>();
        listForRegister.add(EF_REQUEST_1);
        listForRegister.add(EF_REQUEST_1);
        listForRegister.add(EF_REQUEST_2);

        when(mapper.toEntity(EF_REQUEST_1)).thenReturn(EF_ENTITY_1);
        when(mapper.toEntity(EF_REQUEST_2)).thenReturn(EF_ENTITY_2);
        doNothing().when(educationFormDao).saveAll(educationFormEntities);
        doNothing().doThrow(new IncorrectRequestDataException()).when(validator).validateUniqueNameInDB(EF_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(EF_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(EF_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(EF_REQUEST_2);

        educationFormService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(EF_REQUEST_2);
        verify(mapper, times(1)).toEntity(EF_REQUEST_2);
        verify(educationFormDao, times(1)).saveAll(educationFormEntities);
        verify(validator, times(2)).validateUniqueNameInDB(EF_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(EF_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundEducationForm() {
        when(educationFormDao.findById(1)).thenReturn(Optional.of(EF_ENTITY_WITH_ID_1));
        when(mapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);

        EducationFormResponse educationFormResponse = educationFormService.findById(1);

        assertEquals(EF_RESPONSE_WITH_ID_1, educationFormResponse);
        verify(mapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(educationFormDao, times(1)).findById(1);
    }

    @Test
    void findById_inputIncorrectId_expectedException() {
        when(educationFormDao.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> educationFormService.findById(100));

        verify(educationFormDao, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllEducationForms() {
        when(educationFormDao.findAll()).thenReturn(educationFormEntitiesWithId);
        when(mapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(EF_ENTITY_WITH_ID_2)).thenReturn(EF_RESPONSE_WITH_ID_2);

        List<EducationFormResponse> foundCourses = educationFormService.findAll();

        assertEquals(educationFormResponses, foundCourses);
        verify(educationFormDao, times(1)).findAll();
        verify(mapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(EF_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundEducationFormsFromPageOne() {
        when(educationFormDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(educationFormEntitiesWithId);
        when(educationFormDao.count()).thenReturn(2L);
        when(mapper.toResponse(EF_ENTITY_WITH_ID_1)).thenReturn(EF_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(EF_ENTITY_WITH_ID_2)).thenReturn(EF_RESPONSE_WITH_ID_2);

        List<EducationFormResponse> foundCourses = educationFormService.findAll("1");

        assertEquals(educationFormResponses, foundCourses);
        verify(educationFormDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(educationFormDao, times(1)).count();
        verify(mapper, times(1)).toResponse(EF_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(EF_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputEducationFormRequest_expectedNothing() {
        final EducationForm EF_ENTITY_FOR_UPDATE_1 = EducationForm.builder()
                .withId(1)
                .withName("update1")
                .build();

        final EducationFormRequest EF_REQUEST_FOR_UPDATE_1 = new EducationFormRequest();
        EF_REQUEST_FOR_UPDATE_1.setId(1);
        EF_REQUEST_FOR_UPDATE_1.setName("update1");

        doNothing().when(educationFormDao).update(EF_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(EF_REQUEST_FOR_UPDATE_1)).thenReturn(EF_ENTITY_FOR_UPDATE_1);

        educationFormService.edit(EF_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(EF_REQUEST_FOR_UPDATE_1);
        verify(educationFormDao, times(1)).update(EF_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputEducationFormRequestListWhereOneEducationFormHasIncorrectName_expectedNothing() {
        final EducationForm EF_ENTITY_FOR_UPDATE_1 = EducationForm.builder()
                .withId(1)
                .withName("update1")
                .build();

        final EducationFormRequest EF_REQUEST_FOR_UPDATE_1 = new EducationFormRequest();
        EF_REQUEST_FOR_UPDATE_1.setId(1);
        EF_REQUEST_FOR_UPDATE_1.setName("update1");

        final EducationForm EF_ENTITY_FOR_UPDATE_2 = EducationForm.builder()
                .withId(2)
                .withName("update2")
                .build();

        final EducationFormRequest EF_REQUEST_FOR_UPDATE_2 = new EducationFormRequest();
        EF_REQUEST_FOR_UPDATE_2.setId(2);
        EF_REQUEST_FOR_UPDATE_2.setName("update2");

        final EducationFormRequest EF_REQUEST_FOR_UPDATE_INCORRECT = new EducationFormRequest();
        EF_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        EF_REQUEST_FOR_UPDATE_INCORRECT.setName("  ");

        final List<EducationFormRequest> inputList = new ArrayList<>();
        inputList.add(EF_REQUEST_FOR_UPDATE_1);
        inputList.add(EF_REQUEST_FOR_UPDATE_2);
        inputList.add(EF_REQUEST_FOR_UPDATE_INCORRECT);

        final List<EducationForm> listForUpdate = new ArrayList<>();
        listForUpdate.add(EF_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(EF_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator)
                .validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(EF_REQUEST_FOR_UPDATE_1)).thenReturn(EF_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(EF_REQUEST_FOR_UPDATE_2)).thenReturn(EF_ENTITY_FOR_UPDATE_2);
        doNothing().when(educationFormDao).updateAll(listForUpdate);

        educationFormService.editAll(inputList);

        verify(mapper, times(1)).toEntity(EF_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(EF_REQUEST_FOR_UPDATE_2);
        verify(educationFormDao, times(1)).updateAll(listForUpdate);
        verify(validator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(EF_REQUEST_FOR_UPDATE_INCORRECT);
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

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(educationFormDao.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, educationFormService.lastPageNumber());

        verify(educationFormDao, times(1)).count();
    }

}
