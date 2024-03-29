package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.*;
import com.mikhail.tarasevich.university.entity.Lesson;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.LessonMapper;
import com.mikhail.tarasevich.university.service.validator.LessonValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceImplTest {

    @InjectMocks
    LessonServiceImpl lessonService;
    @Mock
    LessonRepository lessonRepository;
    @Mock
    LessonMapper mapper;
    @Mock
    LessonValidator validator;

    private static final Lesson LESSON_ENTITY_1 =
            Lesson.builder().withName("name1").build();
    private static final Lesson LESSON_ENTITY_WITH_ID_1 =
            Lesson.builder().withId(1).withName("name1").build();
    private static final LessonRequest LESSON_REQUEST_1 = new LessonRequest();
    private static final LessonResponse LESSON_RESPONSE_WITH_ID_1 = new LessonResponse();

    private static final Lesson LESSON_ENTITY_2 =
            Lesson.builder().withName("name2").build();
    private static final Lesson LESSON_ENTITY_WITH_ID_2 =
            Lesson.builder().withId(2).withName("name2").build();
    private static final LessonRequest LESSON_REQUEST_2 = new LessonRequest();
    private static final LessonResponse LESSON_RESPONSE_WITH_ID_2 = new LessonResponse();

    private static final List<Lesson> lessonEntities = new ArrayList<>();
    private static final List<Lesson> lessonEntitiesWithId = new ArrayList<>();
    private static final List<LessonResponse> lessonResponses = new ArrayList<>();

    static {
        LESSON_REQUEST_1.setId(0);
        LESSON_REQUEST_1.setName("name1");

        LESSON_RESPONSE_WITH_ID_1.setId(1);
        LESSON_RESPONSE_WITH_ID_1.setName("name1");

        LESSON_REQUEST_2.setId(0);
        LESSON_REQUEST_2.setName("name2");

        LESSON_RESPONSE_WITH_ID_2.setId(2);
        LESSON_RESPONSE_WITH_ID_2.setName("name2");

        lessonEntities.add(LESSON_ENTITY_1);
        lessonEntities.add(LESSON_ENTITY_2);

        lessonEntitiesWithId.add(LESSON_ENTITY_WITH_ID_1);
        lessonEntitiesWithId.add(LESSON_ENTITY_WITH_ID_2);

        lessonResponses.add(LESSON_RESPONSE_WITH_ID_1);
        lessonResponses.add(LESSON_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputLessonRequest_expectedLessonResponseWithId() {
        when(mapper.toEntity(LESSON_REQUEST_1)).thenReturn(LESSON_ENTITY_1);
        when(lessonRepository.save(LESSON_ENTITY_1)).thenReturn(LESSON_ENTITY_WITH_ID_1);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(LESSON_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(LESSON_REQUEST_1);

        LessonResponse lessonResponse = lessonService.register(LESSON_REQUEST_1);

        assertEquals(LESSON_RESPONSE_WITH_ID_1, lessonResponse);
        verify(mapper, times(1)).toEntity(LESSON_REQUEST_1);
        verify(lessonRepository, times(1)).save(LESSON_ENTITY_1);
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(LESSON_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(LESSON_REQUEST_1);
    }

    @Test
    void registerAllLessons_inputLessonRequestListWithRepeatableLesson_expectedNothing() {
        final List<LessonRequest> listForRegister = new ArrayList<>();
        listForRegister.add(LESSON_REQUEST_1);
        listForRegister.add(LESSON_REQUEST_1);
        listForRegister.add(LESSON_REQUEST_2);

        when(mapper.toEntity(LESSON_REQUEST_1)).thenReturn(LESSON_ENTITY_1);
        when(mapper.toEntity(LESSON_REQUEST_2)).thenReturn(LESSON_ENTITY_2);
        doNothing().doThrow(new IncorrectRequestDataException()).when(validator).validateUniqueNameInDB(LESSON_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(LESSON_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(LESSON_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(LESSON_REQUEST_2);

        lessonService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(LESSON_REQUEST_1);
        verify(mapper, times(1)).toEntity(LESSON_REQUEST_2);
        verify(lessonRepository, times(1)).saveAll(lessonEntities);
        verify(validator, times(2)).validateUniqueNameInDB(LESSON_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(LESSON_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(LESSON_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundLesson() {
        when(lessonRepository.findById(1)).thenReturn(Optional.of(LESSON_ENTITY_WITH_ID_1));
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);

        LessonResponse lessonResponse = lessonService.findById(1);

        assertEquals(LESSON_RESPONSE_WITH_ID_1, lessonResponse);
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(lessonRepository, times(1)).findById(1);
    }

    @Test
    void findById_inputIncorrectId_expectedException() {
        when(lessonRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> lessonService.findById(100));

        verify(lessonRepository, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllLessons() {
        when(lessonRepository.findAll()).thenReturn(lessonEntitiesWithId);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_2)).thenReturn(LESSON_RESPONSE_WITH_ID_2);

        List<LessonResponse> foundCourses = lessonService.findAll();

        assertEquals(lessonResponses, foundCourses);
        verify(lessonRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundLessonsFromPageOne() {
        Page<Lesson> pageOfLessonEntitiesWithId = new PageImpl<>(lessonEntitiesWithId);

        when(lessonRepository
                .findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("startTime"))))
                .thenReturn(pageOfLessonEntitiesWithId);
        when(lessonRepository.count()).thenReturn(2L);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_2)).thenReturn(LESSON_RESPONSE_WITH_ID_2);

        List<LessonResponse> foundCourses = lessonService.findAll("1");

        assertEquals(lessonResponses, foundCourses);
        verify(lessonRepository, times(1)).findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("startTime")));
        verify(lessonRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputLessonRequest_expectedNothing() {
        final Lesson LESSON_ENTITY_FOR_UPDATE_1 = Lesson.builder().withId(1).withName("update1").build();
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_1 = new LessonRequest();
        LESSON_REQUEST_FOR_UPDATE_1.setId(0);
        LESSON_REQUEST_FOR_UPDATE_1.setName("update1");

        when(mapper.toEntity(LESSON_REQUEST_FOR_UPDATE_1)).thenReturn(LESSON_ENTITY_FOR_UPDATE_1);

        lessonService.edit(LESSON_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(LESSON_REQUEST_FOR_UPDATE_1);
        verify(lessonRepository, times(1)).save(LESSON_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputLessonRequestListWhereOneCourseHasIncorrectName_expectedNothing() {
        final Lesson LESSON_ENTITY_FOR_UPDATE_1 = Lesson.builder().withId(1).withName("update1").build();
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_1 = new LessonRequest();
        LESSON_REQUEST_FOR_UPDATE_1.setId(1);
        LESSON_REQUEST_FOR_UPDATE_1.setName("update1");

        final Lesson LESSON_ENTITY_FOR_UPDATE_2 = Lesson.builder().withId(2).withName("update2").build();
        final LessonRequest LESSON_REQUEST_FOR_UPDATE_2 = new LessonRequest();
        LESSON_REQUEST_FOR_UPDATE_2.setId(2);
        LESSON_REQUEST_FOR_UPDATE_2.setName("update2");

        final LessonRequest LESSON_REQUEST_FOR_UPDATE_INCORRECT = new LessonRequest();
        LESSON_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        LESSON_REQUEST_FOR_UPDATE_INCORRECT.setName("  ");

        final List<LessonRequest> inputList = new ArrayList<>();
        inputList.add(LESSON_REQUEST_FOR_UPDATE_1);
        inputList.add(LESSON_REQUEST_FOR_UPDATE_2);
        inputList.add(LESSON_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Lesson> listForUpdate = new ArrayList<>();
        listForUpdate.add(LESSON_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(LESSON_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator)
                .validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(LESSON_REQUEST_FOR_UPDATE_1)).thenReturn(LESSON_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(LESSON_REQUEST_FOR_UPDATE_2)).thenReturn(LESSON_ENTITY_FOR_UPDATE_2);

        lessonService.editAll(inputList);

        verify(mapper, times(1)).toEntity(LESSON_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(LESSON_REQUEST_FOR_UPDATE_2);
        verify(lessonRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(LESSON_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputLessonId_expectedSuccessDelete() {
        int id = 1;

        when(lessonRepository.findById(id)).thenReturn(Optional.of(LESSON_ENTITY_1));
        doNothing().when(lessonRepository).deleteById(id);

        boolean result = lessonService.deleteById(1);

        assertTrue(result);
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_inputLessonId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = lessonService.deleteById(1);

        assertFalse(result);
        verify(lessonRepository, times(1)).findById(id);
        verify(lessonRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputLessonsIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(lessonRepository).deleteAllByIdInBatch(ids);

        boolean result = lessonService.deleteByIds(ids);

        assertTrue(result);
        verify(lessonRepository, times(1)).deleteAllByIdInBatch(ids);
    }

    @Test
    void findLessonsRelateToGroup() {
        when(lessonRepository.findLessonByGroupId(1)).thenReturn(lessonEntitiesWithId);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_1)).thenReturn(LESSON_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(LESSON_ENTITY_WITH_ID_2)).thenReturn(LESSON_RESPONSE_WITH_ID_2);

        List<LessonResponse> foundLessons = lessonService.findLessonsRelateToGroup(1);

        assertEquals(lessonResponses, foundLessons);
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(LESSON_ENTITY_WITH_ID_2);
        verify(lessonRepository, times(1)).findLessonByGroupId(1);
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(lessonRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, lessonService.lastPageNumber());

        verify(lessonRepository, times(1)).count();
    }

}
