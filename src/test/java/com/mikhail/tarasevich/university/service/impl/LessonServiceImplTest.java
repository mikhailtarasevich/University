package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dto.*;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import com.mikhail.tarasevich.university.validator.LessonValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @InjectMocks
    LessonServiceImpl lessonService;
    @Mock
    LessonDao lessonDao;
    @Mock
    LessonMapper lessonMapper;
    @Mock
    LessonValidator lessonValidator;

    private static final Lesson LESSON_ENTITY_1 =
            Lesson.builder().withName("name1").build();
    private static final Lesson LESSON_ENTITY_WITH_ID_1 =
            Lesson.builder().withId(1).withName("name1").build();
    private static final LessonRequest LESSON_REQUEST_1 =
            new LessonRequest(0, "name1", null, null, null, null, null);
    private static final LessonResponse LESSON_RESPONSE_WITH_ID_1 =
            new LessonResponse(1, "name1", null, null, null, null, null);

    private static final Lesson LESSON_ENTITY_2 =
            Lesson.builder().withName("name2").build();
    private static final Lesson LESSON_ENTITY_WITH_ID_2 =
            Lesson.builder().withId(2).withName("name2").build();
    private static final LessonRequest LESSON_REQUEST_2 =
            new LessonRequest(0, "name2", null, null, null, null, null);
    private static final LessonResponse LESSON_RESPONSE_WITH_ID_2 =
            new LessonResponse(2, "name2", null, null, null, null, null);

    private final List<Lesson> lessonEntities = new ArrayList<>();
    private final List<Lesson> lessonEntitiesWithId = new ArrayList<>();
    private final List<LessonResponse> lessonResponses = new ArrayList<>();

    {
        lessonEntities.add(LESSON_ENTITY_1);
        lessonEntities.add(LESSON_ENTITY_2);

        lessonEntitiesWithId.add(LESSON_ENTITY_WITH_ID_1);
        lessonEntitiesWithId.add(LESSON_ENTITY_WITH_ID_2);

        lessonResponses.add(LESSON_RESPONSE_WITH_ID_1);
        lessonResponses.add(LESSON_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputCourseRequest_expectedCourseResponseWithId() {
        when(lessonMapper.toEntity(LESSON_REQUEST_1)).thenReturn(LESSON_ENTITY_1);
        when(lessonDao.save(LESSON_ENTITY_1)).thenReturn(LESSON_ENTITY_WITH_ID_1);
        when(lessonMapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        doNothing().when(lessonValidator).validateUniqueNameInDB(LESSON_REQUEST_1);
        doNothing().when(lessonValidator).validateNameNotNullOrEmpty(LESSON_REQUEST_1);

        LessonResponse lessonResponse = lessonService.register(LESSON_REQUEST_1);

        assertEquals(LESSON_RESPONSE_WITH_ID_1, lessonResponse);
        verify(lessonMapper, times(1)).toEntity(LESSON_REQUEST_1);
        verify(lessonDao, times(1)).save(LESSON_ENTITY_1);
        verify(lessonMapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(lessonValidator, times(1)).validateUniqueNameInDB(LESSON_REQUEST_1);
        verify(lessonValidator, times(1)).validateUniqueNameInDB(LESSON_REQUEST_1);
    }

    @Test
    void registerAllCourses_inputCourseRequestListWithRepeatableCourse_expectedNothing() {
        final List<LessonRequest> listForRegister = new ArrayList<>();
        listForRegister.add(LESSON_REQUEST_1);
        listForRegister.add(LESSON_REQUEST_1);
        listForRegister.add(LESSON_REQUEST_2);

        when(lessonMapper.toEntity(LESSON_REQUEST_1)).thenReturn(LESSON_ENTITY_1);
        when(lessonMapper.toEntity(LESSON_REQUEST_2)).thenReturn(LESSON_ENTITY_2);
        doNothing().when(lessonDao).saveAll(lessonEntities);
        doNothing().doThrow(new IncorrectRequestData()).when(lessonValidator).validateUniqueNameInDB(LESSON_REQUEST_1);
        doNothing().when(lessonValidator).validateNameNotNullOrEmpty(LESSON_REQUEST_1);
        doNothing().when(lessonValidator).validateUniqueNameInDB(LESSON_REQUEST_2);
        doNothing().when(lessonValidator).validateNameNotNullOrEmpty(LESSON_REQUEST_2);

        lessonService.registerAll(listForRegister);

        verify(lessonMapper, times(1)).toEntity(LESSON_REQUEST_1);
        verify(lessonMapper, times(1)).toEntity(LESSON_REQUEST_2);
        verify(lessonDao, times(1)).saveAll(lessonEntities);
        verify(lessonValidator, times(2)).validateUniqueNameInDB(LESSON_REQUEST_1);
        verify(lessonValidator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_1);
        verify(lessonValidator, times(1)).validateUniqueNameInDB(LESSON_REQUEST_2);
        verify(lessonValidator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundCourse() {
        when(lessonDao.findById(1)).thenReturn(Optional.of(LESSON_ENTITY_WITH_ID_1));
        when(lessonMapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);

        Optional<LessonResponse> lessonResponse = lessonService.findById(1);

        assertEquals(Optional.of(LESSON_RESPONSE_WITH_ID_1), lessonResponse);
        verify(lessonMapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(lessonDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundCoursesFromPageOne() {
        when(lessonDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(lessonEntitiesWithId);
        when(lessonDao.count()).thenReturn(2L);
        when(lessonMapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        when(lessonMapper.toResponse(LESSON_ENTITY_WITH_ID_2)).thenReturn(LESSON_RESPONSE_WITH_ID_2);

        List<LessonResponse> foundCourses = lessonService.findAll("1");

        assertEquals(lessonResponses, foundCourses);
        verify(lessonDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(lessonDao, times(1)).count();
        verify(lessonMapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(lessonMapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputCourseRequest_expectedNothing() {
        final Lesson LESSON_ENTITY_FOR_UPDATE_1 =
                Lesson.builder().withId(1).withName("update1").build();
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_1 =
                new LessonRequest(0, "name1", null, null, null, null, null);

        doNothing().when(lessonDao).update(LESSON_ENTITY_FOR_UPDATE_1);
        when(lessonMapper.toEntity(LESSON_REQUEST_FOR_UPDATE_1)).thenReturn(LESSON_ENTITY_FOR_UPDATE_1);

        lessonService.edit(LESSON_REQUEST_FOR_UPDATE_1);

        verify(lessonMapper, times(1)).toEntity(LESSON_REQUEST_FOR_UPDATE_1);
        verify(lessonDao, times(1)).update(LESSON_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputCourseRequestListWhereOneCourseHasIncorrectName_expectedNothing() {
        final Lesson LESSON_ENTITY_FOR_UPDATE_1 =
                Lesson.builder().withId(1).withName("update1").build();
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_1 =
                new LessonRequest(0, "name1", null, null, null, null, null);
        final Lesson LESSON_ENTITY_FOR_UPDATE_2 =
                Lesson.builder().withId(2).withName("update2").build();
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_2 =
                new LessonRequest(0, "name2", null, null, null, null, null);
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_INCORRECT =
                new LessonRequest(3, " ", null, null, null, null, null);

        final List<LessonRequest> inputList = new ArrayList<>();
        inputList.add(LESSON_REQUEST_FOR_UPDATE_1);
        inputList.add(LESSON_REQUEST_FOR_UPDATE_2);
        inputList.add(LESSON_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Lesson> listForUpdate = new ArrayList<>();
        listForUpdate.add(LESSON_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(LESSON_ENTITY_FOR_UPDATE_2);

        doNothing().when(lessonValidator).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_1);
        doNothing().when(lessonValidator).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData()).when(lessonValidator).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_INCORRECT);

        when(lessonMapper.toEntity(LESSON_REQUEST_FOR_UPDATE_1)).thenReturn(LESSON_ENTITY_FOR_UPDATE_1);
        when(lessonMapper.toEntity(LESSON_REQUEST_FOR_UPDATE_2)).thenReturn(LESSON_ENTITY_FOR_UPDATE_2);

        doNothing().when(lessonDao).updateAll(listForUpdate);

        lessonService.editAll(inputList);

        verify(lessonMapper, times(1)).toEntity(LESSON_REQUEST_FOR_UPDATE_1);
        verify(lessonMapper, times(1)).toEntity(LESSON_REQUEST_FOR_UPDATE_2);
        verify(lessonDao, times(1)).updateAll(listForUpdate);
        verify(lessonValidator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_1);
        verify(lessonValidator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_2);
        verify(lessonValidator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputCourseId_expectedSuccessDelete() {
        int id = 1;

        when(lessonDao.findById(id)).thenReturn(Optional.of(LESSON_ENTITY_1));
        when(lessonDao.deleteById(id)).thenReturn(true);

        boolean result = lessonService.deleteById(1);

        assertTrue(result);
        verify(lessonDao, times(1)).findById(id);
        verify(lessonDao, times(1)).deleteById(id);
    }

    @Test
    void deleteById_inputCourseId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(lessonDao.findById(id)).thenReturn(Optional.empty());

        boolean result = lessonService.deleteById(1);

        assertFalse(result);
        verify(lessonDao, times(1)).findById(id);
        verify(lessonDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputCoursesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        when(lessonDao.deleteByIds(ids)).thenReturn(true);

        boolean result = lessonService.deleteByIds(ids);

        assertTrue(result);
        verify(lessonDao, times(1)).deleteByIds(ids);
    }

    @Test
    void findLessonsRelateToGroup() {
        when(lessonDao.findLessonsRelateToGroup(1)).thenReturn(lessonEntitiesWithId);
        when(lessonMapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        when(lessonMapper.toResponse(LESSON_ENTITY_WITH_ID_2)).thenReturn(LESSON_RESPONSE_WITH_ID_2);

        List<LessonResponse> foundLessons = lessonService.findLessonsRelateToGroup(1);

        assertEquals(lessonResponses, foundLessons);
        verify(lessonMapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(lessonMapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_2);
        verify(lessonDao, times(1)).findLessonsRelateToGroup(1);
    }

}
