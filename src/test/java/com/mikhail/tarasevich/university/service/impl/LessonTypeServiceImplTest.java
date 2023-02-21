package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.dto.LessonTypeRequest;
import com.mikhail.tarasevich.university.dto.LessonTypeResponse;
import com.mikhail.tarasevich.university.entity.LessonType;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.LessonTypeMapper;
import com.mikhail.tarasevich.university.validator.LessonTypeValidator;
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
class LessonTypeServiceImplTest {

    @InjectMocks
    LessonTypeServiceImpl lessonTypeService;
    @Mock
    LessonTypeDao lessonTypeDao;
    @Mock
    LessonDao lessonDao;
    @Mock
    LessonTypeMapper lessonTypeMapper;
    @Mock
    LessonTypeValidator lessonTypeValidator;

    private static final LessonType LESSON_TYPE_ENTITY_1 = LessonType.builder().withName("name1").build();
    private static final LessonType LESSON_TYPE_ENTITY_WITH_ID_1 = LessonType.builder()
            .withId(1)
            .withName("name1")
            .build();
    private static final LessonTypeRequest LESSON_TYPE_REQUEST_1 = new LessonTypeRequest();
    private static final LessonTypeResponse LESSON_TYPE_RESPONSE_WITH_ID_1 = new LessonTypeResponse();


    private static final LessonType LESSON_TYPE_ENTITY_2 =
            LessonType.builder().withName("name2").build();
    private static final LessonType LESSON_TYPE_ENTITY_WITH_ID_2 =
            LessonType.builder().withId(2).withName("name2").build();
    private static final LessonTypeRequest LESSON_TYPE_REQUEST_2 = new LessonTypeRequest();
    private static final LessonTypeResponse LESSON_TYPE_RESPONSE_WITH_ID_2 = new LessonTypeResponse();

    private static final List<LessonType> lessonTypeEntities = new ArrayList<>();
    private static final List<LessonType> lessonTypeEntitiesWithId = new ArrayList<>();
    private static final List<LessonTypeResponse> lessonTypeResponses = new ArrayList<>();

    static {
        LESSON_TYPE_REQUEST_1.setId(0);
        LESSON_TYPE_REQUEST_1.setName("name1");

        LESSON_TYPE_RESPONSE_WITH_ID_1.setId(1);
        LESSON_TYPE_RESPONSE_WITH_ID_1.setName("name1");

        LESSON_TYPE_REQUEST_2.setId(0);
        LESSON_TYPE_REQUEST_2.setName("name2");

        LESSON_TYPE_RESPONSE_WITH_ID_2.setId(2);
        LESSON_TYPE_RESPONSE_WITH_ID_2.setName("name2");

        lessonTypeEntities.add(LESSON_TYPE_ENTITY_1);
        lessonTypeEntities.add(LESSON_TYPE_ENTITY_2);

        lessonTypeEntitiesWithId.add(LESSON_TYPE_ENTITY_WITH_ID_1);
        lessonTypeEntitiesWithId.add(LESSON_TYPE_ENTITY_WITH_ID_2);

        lessonTypeResponses.add(LESSON_TYPE_RESPONSE_WITH_ID_1);
        lessonTypeResponses.add(LESSON_TYPE_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputLessonTypeRequest_expectedLessonTypeResponseWithId() {
        when(lessonTypeMapper.toEntity(LESSON_TYPE_REQUEST_1)).thenReturn(LESSON_TYPE_ENTITY_1);
        when(lessonTypeDao.save(LESSON_TYPE_ENTITY_1)).thenReturn(LESSON_TYPE_ENTITY_WITH_ID_1);
        when(lessonTypeMapper.toResponse(LESSON_TYPE_ENTITY_WITH_ID_1)).thenReturn(LESSON_TYPE_RESPONSE_WITH_ID_1);
        doNothing().when(lessonTypeValidator).validateUniqueNameInDB(LESSON_TYPE_REQUEST_1);
        doNothing().when(lessonTypeValidator).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_1);

        LessonTypeResponse lessonTypeResponse = lessonTypeService.register(LESSON_TYPE_REQUEST_1);

        assertEquals(LESSON_TYPE_RESPONSE_WITH_ID_1, lessonTypeResponse);
        verify(lessonTypeMapper, times(1)).toEntity(LESSON_TYPE_REQUEST_1);
        verify(lessonTypeDao, times(1)).save(LESSON_TYPE_ENTITY_1);
        verify(lessonTypeMapper, times(1)).toResponse(LESSON_TYPE_ENTITY_WITH_ID_1);
        verify(lessonTypeValidator, times(1)).validateUniqueNameInDB(LESSON_TYPE_REQUEST_1);
        verify(lessonTypeValidator, times(1)).validateUniqueNameInDB(LESSON_TYPE_REQUEST_1);
    }

    @Test
    void registerAllLessonTypes_inputLessonTypeRequestListWithRepeatableLessonType_expectedNothing() {
        final List<LessonTypeRequest> listForRegister = new ArrayList<>();
        listForRegister.add(LESSON_TYPE_REQUEST_1);
        listForRegister.add(LESSON_TYPE_REQUEST_1);
        listForRegister.add(LESSON_TYPE_REQUEST_2);

        when(lessonTypeMapper.toEntity(LESSON_TYPE_REQUEST_1)).thenReturn(LESSON_TYPE_ENTITY_1);
        when(lessonTypeMapper.toEntity(LESSON_TYPE_REQUEST_2)).thenReturn(LESSON_TYPE_ENTITY_2);
        doNothing().when(lessonTypeDao).saveAll(lessonTypeEntities);
        doNothing().doThrow(new IncorrectRequestData()).when(lessonTypeValidator)
                .validateUniqueNameInDB(LESSON_TYPE_REQUEST_1);
        doNothing().when(lessonTypeValidator).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_1);
        doNothing().when(lessonTypeValidator).validateUniqueNameInDB(LESSON_TYPE_REQUEST_2);
        doNothing().when(lessonTypeValidator).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_2);

        lessonTypeService.registerAll(listForRegister);

        verify(lessonTypeMapper, times(1)).toEntity(LESSON_TYPE_REQUEST_1);
        verify(lessonTypeMapper, times(1)).toEntity(LESSON_TYPE_REQUEST_2);
        verify(lessonTypeDao, times(1)).saveAll(lessonTypeEntities);
        verify(lessonTypeValidator, times(2)).validateUniqueNameInDB(LESSON_TYPE_REQUEST_1);
        verify(lessonTypeValidator, times(1)).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_1);
        verify(lessonTypeValidator, times(1)).validateUniqueNameInDB(LESSON_TYPE_REQUEST_2);
        verify(lessonTypeValidator, times(1)).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundLessonType() {
        when(lessonTypeDao.findById(1)).thenReturn(Optional.of(LESSON_TYPE_ENTITY_WITH_ID_1));
        when(lessonTypeMapper.toResponse(LESSON_TYPE_ENTITY_WITH_ID_1)).thenReturn(LESSON_TYPE_RESPONSE_WITH_ID_1);

        Optional<LessonTypeResponse> lessonTypeResponse = lessonTypeService.findById(1);

        assertEquals(Optional.of(LESSON_TYPE_RESPONSE_WITH_ID_1), lessonTypeResponse);
        verify(lessonTypeMapper, times(1)).toResponse(LESSON_TYPE_ENTITY_WITH_ID_1);
        verify(lessonTypeDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundLessonTypesFromPageOne() {
        when(lessonTypeDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE))
                .thenReturn(lessonTypeEntitiesWithId);
        when(lessonTypeDao.count()).thenReturn(2L);
        when(lessonTypeMapper.toResponse(LESSON_TYPE_ENTITY_WITH_ID_1)).thenReturn(LESSON_TYPE_RESPONSE_WITH_ID_1);
        when(lessonTypeMapper.toResponse(LESSON_TYPE_ENTITY_WITH_ID_2)).thenReturn(LESSON_TYPE_RESPONSE_WITH_ID_2);

        List<LessonTypeResponse> foundLessonTypes = lessonTypeService.findAll("1");

        assertEquals(lessonTypeResponses, foundLessonTypes);
        verify(lessonTypeDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(lessonTypeDao, times(1)).count();
        verify(lessonTypeMapper, times(1)).toResponse(LESSON_TYPE_ENTITY_WITH_ID_1);
        verify(lessonTypeMapper, times(1)).toResponse(LESSON_TYPE_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputLessonTypeRequest_expectedNothing() {
        final LessonType LESSON_TYPE_ENTITY_FOR_UPDATE_1 =
                LessonType.builder().withId(1).withName("update1").build();
        final LessonTypeRequest LESSON_TYPE_REQUEST_FOR_UPDATE_1 = new LessonTypeRequest();
        LESSON_TYPE_REQUEST_FOR_UPDATE_1.setId(1);
        LESSON_TYPE_REQUEST_FOR_UPDATE_1.setName("update1");

        doNothing().when(lessonTypeDao).update(LESSON_TYPE_ENTITY_FOR_UPDATE_1);
        when(lessonTypeMapper.toEntity(LESSON_TYPE_REQUEST_FOR_UPDATE_1)).thenReturn(LESSON_TYPE_ENTITY_FOR_UPDATE_1);

        lessonTypeService.edit(LESSON_TYPE_REQUEST_FOR_UPDATE_1);

        verify(lessonTypeMapper, times(1)).toEntity(LESSON_TYPE_REQUEST_FOR_UPDATE_1);
        verify(lessonTypeDao, times(1)).update(LESSON_TYPE_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputLessonTypeRequestListWhereOneLessonTypeHasIncorrectName_expectedNothing() {
        final LessonType LESSON_TYPE_ENTITY_FOR_UPDATE_1 =
                LessonType.builder().withId(1).withName("update1").build();
        final LessonTypeRequest LESSON_TYPE_REQUEST_FOR_UPDATE_1 = new LessonTypeRequest();
        LESSON_TYPE_REQUEST_FOR_UPDATE_1.setId(1);
        LESSON_TYPE_REQUEST_FOR_UPDATE_1.setName("update1");

        final LessonType LESSON_TYPE_ENTITY_FOR_UPDATE_2 =
                LessonType.builder().withId(2).withName("update2").build();
        final LessonTypeRequest LESSON_TYPE_REQUEST_FOR_UPDATE_2 = new LessonTypeRequest();
        LESSON_TYPE_REQUEST_FOR_UPDATE_2.setId(2);
        LESSON_TYPE_REQUEST_FOR_UPDATE_2.setName("update2");

        final LessonTypeRequest LESSON_TYPE_REQUEST_FOR_UPDATE_INCORRECT = new LessonTypeRequest();
        LESSON_TYPE_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        LESSON_TYPE_REQUEST_FOR_UPDATE_INCORRECT.setName("   ");

        final List<LessonTypeRequest> inputList = new ArrayList<>();
        inputList.add(LESSON_TYPE_REQUEST_FOR_UPDATE_1);
        inputList.add(LESSON_TYPE_REQUEST_FOR_UPDATE_2);
        inputList.add(LESSON_TYPE_REQUEST_FOR_UPDATE_INCORRECT);

        final List<LessonType> listForUpdate = new ArrayList<>();
        listForUpdate.add(LESSON_TYPE_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(LESSON_TYPE_ENTITY_FOR_UPDATE_2);

        doNothing().when(lessonTypeValidator).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_FOR_UPDATE_1);
        doNothing().when(lessonTypeValidator).validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData()).when(lessonTypeValidator)
                .validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_FOR_UPDATE_INCORRECT);

        when(lessonTypeMapper.toEntity(LESSON_TYPE_REQUEST_FOR_UPDATE_1)).thenReturn(LESSON_TYPE_ENTITY_FOR_UPDATE_1);
        when(lessonTypeMapper.toEntity(LESSON_TYPE_REQUEST_FOR_UPDATE_2)).thenReturn(LESSON_TYPE_ENTITY_FOR_UPDATE_2);

        doNothing().when(lessonTypeDao).updateAll(listForUpdate);

        lessonTypeService.editAll(inputList);

        verify(lessonTypeMapper, times(1)).toEntity(LESSON_TYPE_REQUEST_FOR_UPDATE_1);
        verify(lessonTypeMapper, times(1)).toEntity(LESSON_TYPE_REQUEST_FOR_UPDATE_2);
        verify(lessonTypeDao, times(1)).updateAll(listForUpdate);
        verify(lessonTypeValidator, times(1))
                .validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_FOR_UPDATE_1);
        verify(lessonTypeValidator, times(1))
                .validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_FOR_UPDATE_2);
        verify(lessonTypeValidator, times(1))
                .validateNameNotNullOrEmpty(LESSON_TYPE_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputLessonTypeId_expectedSuccessDelete() {
        int id = 1;

        when(lessonTypeDao.findById(id)).thenReturn(Optional.of(LESSON_TYPE_ENTITY_1));
        doNothing().when(lessonDao).unbindLessonsFromLessonType(id);
        when(lessonTypeDao.deleteById(id)).thenReturn(true);

        boolean result = lessonTypeService.deleteById(1);

        assertTrue(result);
        verify(lessonTypeDao, times(1)).findById(id);
        verify(lessonTypeDao, times(1)).deleteById(id);
        verify(lessonDao, times(1)).unbindLessonsFromLessonType(id);
    }

    @Test
    void deleteById_inputLessonTypeId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(lessonTypeDao.findById(id)).thenReturn(Optional.empty());

        boolean result = lessonTypeService.deleteById(1);

        assertFalse(result);
        verify(lessonTypeDao, times(1)).findById(id);
        verifyNoInteractions(lessonDao);
        verify(lessonTypeDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputLessonTypesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(lessonDao).unbindLessonsFromLessonType(1);
        doNothing().when(lessonDao).unbindLessonsFromLessonType(2);
        when(lessonTypeDao.deleteByIds(ids)).thenReturn(true);

        boolean result = lessonTypeService.deleteByIds(ids);

        assertTrue(result);
        verify(lessonTypeDao, times(1)).deleteByIds(ids);
        verify(lessonDao, times(1)).unbindLessonsFromLessonType(1);
        verify(lessonDao, times(1)).unbindLessonsFromLessonType(2);
    }

}
