package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.repository.TeacherTitleRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.mapper.TeacherTitleMapper;
import com.mikhail.tarasevich.university.service.validator.TeacherTitleValidator;
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
class TeacherTitleServiceImplTest {

    @InjectMocks
    TeacherTitleServiceImpl teacherTitleService;
    @Mock
    TeacherTitleRepository teacherTitleRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    TeacherTitleMapper mapper;
    @Mock
    TeacherTitleValidator validator;

    private static final TeacherTitle TEACHER_TITLE_ENTITY_1 = TeacherTitle.builder().withName("name1").build();
    private static final TeacherTitle TEACHER_TITLE_ENTITY_WITH_ID_1 = TeacherTitle.builder()
            .withId(1)
            .withName("name1")
            .build();
    private static final TeacherTitleRequest TEACHER_TITLE_REQUEST_1 = new TeacherTitleRequest();
    private static final TeacherTitleResponse TEACHER_TITLE_WITH_ID_1 = new TeacherTitleResponse();

    private static final TeacherTitle TEACHER_TITLE_ENTITY_2 = TeacherTitle.builder().withName("name2").build();
    private static final TeacherTitle TEACHER_TITLE_ENTITY_WITH_ID_2 = TeacherTitle.builder()
            .withId(2)
            .withName("name2")
            .build();
    private static final TeacherTitleRequest TEACHER_TITLE_REQUEST_2 = new TeacherTitleRequest();
    private static final TeacherTitleResponse TEACHER_TITLE_WITH_ID_2 = new TeacherTitleResponse();

    private static final List<TeacherTitle> teacherTitleEntities = new ArrayList<>();
    private static final List<TeacherTitle> teacherTitleEntitiesWithId = new ArrayList<>();
    private static final List<TeacherTitleResponse> teacherTitleResponses = new ArrayList<>();

    static {
        TEACHER_TITLE_REQUEST_1.setId(0);
        TEACHER_TITLE_REQUEST_1.setName("name1");

        TEACHER_TITLE_WITH_ID_1.setId(1);
        TEACHER_TITLE_WITH_ID_1.setName("name1");

        TEACHER_TITLE_REQUEST_2.setId(0);
        TEACHER_TITLE_REQUEST_2.setName("name2");

        TEACHER_TITLE_WITH_ID_2.setId(2);
        TEACHER_TITLE_WITH_ID_2.setName("name2");

        teacherTitleEntities.add(TEACHER_TITLE_ENTITY_1);
        teacherTitleEntities.add(TEACHER_TITLE_ENTITY_2);

        teacherTitleEntitiesWithId.add(TEACHER_TITLE_ENTITY_WITH_ID_1);
        teacherTitleEntitiesWithId.add(TEACHER_TITLE_ENTITY_WITH_ID_2);

        teacherTitleResponses.add(TEACHER_TITLE_WITH_ID_1);
        teacherTitleResponses.add(TEACHER_TITLE_WITH_ID_2);
    }

    @Test
    void register_inputTeacherTitleRequest_expectedTeacherTitleResponseWithId() {
        when(mapper.toEntity(TEACHER_TITLE_REQUEST_1)).thenReturn(TEACHER_TITLE_ENTITY_1);
        when(teacherTitleRepository.save(TEACHER_TITLE_ENTITY_1)).thenReturn(TEACHER_TITLE_ENTITY_WITH_ID_1);
        when(mapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_1);

        TeacherTitleResponse teacherTitleResponse = teacherTitleService.register(TEACHER_TITLE_REQUEST_1);

        assertEquals(TEACHER_TITLE_WITH_ID_1, teacherTitleResponse);
        verify(mapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_1);
        verify(teacherTitleRepository, times(1)).save(TEACHER_TITLE_ENTITY_1);
        verify(mapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
    }

    @Test
    void registerAllCourses_inputTeacherTitleRequestListWithRepeatableCourse_expectedNothing() {
        final List<TeacherTitleRequest> listForRegister = new ArrayList<>();
        listForRegister.add(TEACHER_TITLE_REQUEST_1);
        listForRegister.add(TEACHER_TITLE_REQUEST_1);
        listForRegister.add(TEACHER_TITLE_REQUEST_2);

        when(mapper.toEntity(TEACHER_TITLE_REQUEST_1)).thenReturn(TEACHER_TITLE_ENTITY_1);
        when(mapper.toEntity(TEACHER_TITLE_REQUEST_2)).thenReturn(TEACHER_TITLE_ENTITY_2);
        doNothing().doThrow(new IncorrectRequestDataException()).when(validator)
                .validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_2);

        teacherTitleService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_1);
        verify(mapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_2);
        verify(teacherTitleRepository, times(1)).saveAll(teacherTitleEntities);
        verify(validator, times(2))
                .validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_1);
        verify(validator, times(1))
                .validateUniqueNameInDB(TEACHER_TITLE_REQUEST_2);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundTeacherTitle() {
        when(teacherTitleRepository.findById(1)).thenReturn(Optional.of(TEACHER_TITLE_ENTITY_WITH_ID_1));
        when(mapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);

        TeacherTitleResponse teacherTitleResponse = teacherTitleService.findById(1);

        assertEquals(TEACHER_TITLE_WITH_ID_1, teacherTitleResponse);
        verify(mapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
        verify(teacherTitleRepository, times(1)).findById(1);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllTeacherTitles() {
        when(teacherTitleRepository.findAll()).thenReturn(teacherTitleEntitiesWithId);
        when(mapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);
        when(mapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_2)).thenReturn(TEACHER_TITLE_WITH_ID_2);

        List<TeacherTitleResponse> foundTeacherTitles = teacherTitleService.findAll();

        assertEquals(teacherTitleResponses, foundTeacherTitles);
        verify(teacherTitleRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_2);
    }

//    @Test
//    void findAll_inputPageOne_expectedFoundTeacherTitlesFromPageOne() {
//        when(teacherTitleRepository.findAll(1, AbstractPageableService.ITEMS_PER_PAGE))
//                .thenReturn(teacherTitleEntitiesWithId);
//        when(teacherTitleRepository.count()).thenReturn(2L);
//        when(mapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);
//        when(mapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_2)).thenReturn(TEACHER_TITLE_WITH_ID_2);
//
//        List<TeacherTitleResponse> foundTeacherTitles = teacherTitleService.findAll("1");
//
//        assertEquals(teacherTitleResponses, foundTeacherTitles);
//        verify(teacherTitleRepository, times(1))
//                .findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
//        verify(teacherTitleRepository, times(1)).count();
//        verify(mapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
//        verify(mapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_2);
//    }

    @Test
    void edit_inputTeacherTitleRequest_expectedNothing() {
        final TeacherTitle TEACHER_TITLE_ENTITY_FOR_UPDATE_1 = TeacherTitle.builder()
                .withId(1)
                .withName("update1")
                .build();
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_1 = new TeacherTitleRequest();
        TEACHER_TITLE_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_TITLE_REQUEST_FOR_UPDATE_1.setName("update1");

        when(mapper.toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1))
                .thenReturn(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);

        teacherTitleService.edit(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        verify(teacherTitleRepository, times(1)).save(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputTeacherTitleRequestListWhereOneTeacherTitleHasIncorrectName_expectedNothing() {
        final TeacherTitle TEACHER_TITLE_ENTITY_FOR_UPDATE_1 = TeacherTitle.builder()
                .withId(1)
                .withName("update1")
                .build();
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_1 = new TeacherTitleRequest();
        TEACHER_TITLE_REQUEST_FOR_UPDATE_1.setId(1);
        TEACHER_TITLE_REQUEST_FOR_UPDATE_1.setName("update1");

        final TeacherTitle TEACHER_TITLE_ENTITY_FOR_UPDATE_2 = TeacherTitle.builder()
                .withId(2)
                .withName("update2")
                .build();
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_2 = new TeacherTitleRequest();
        TEACHER_TITLE_REQUEST_FOR_UPDATE_2.setId(2);
        TEACHER_TITLE_REQUEST_FOR_UPDATE_2.setName("update2");

        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT = new TeacherTitleRequest();
        TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT.setName("  ");

        final List<TeacherTitleRequest> inputList = new ArrayList<>();
        inputList.add(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        inputList.add(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        inputList.add(TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT);

        final List<TeacherTitle> listForUpdate = new ArrayList<>();
        listForUpdate.add(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(TEACHER_TITLE_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator)
                .validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1))
                .thenReturn(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_2))
                .thenReturn(TEACHER_TITLE_ENTITY_FOR_UPDATE_2);

        teacherTitleService.editAll(inputList);

        verify(mapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        verify(teacherTitleRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputTeacherTitleId_expectedSuccessDelete() {
        int id = 1;

        when(teacherTitleRepository.findById(id)).thenReturn(Optional.of(TEACHER_TITLE_ENTITY_1));
        doNothing().when(teacherRepository).unbindTeachersFromTeacherTitle(id);
        doNothing().when(teacherTitleRepository).deleteById(id);

        boolean result = teacherTitleService.deleteById(1);

        assertTrue(result);
        verify(teacherTitleRepository, times(1)).findById(id);
        verify(teacherTitleRepository, times(1)).deleteById(id);
        verify(teacherRepository, times(1)).unbindTeachersFromTeacherTitle(id);
    }

    @Test
    void deleteById_inputTeacherTitleId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(teacherTitleRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = teacherTitleService.deleteById(1);

        assertFalse(result);
        verify(teacherTitleRepository, times(1)).findById(id);
        verifyNoInteractions(teacherRepository);
        verify(teacherTitleRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputCoursesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(teacherRepository).unbindTeachersFromTeacherTitle(1);
        doNothing().when(teacherRepository).unbindTeachersFromTeacherTitle(2);
        doNothing().when(teacherTitleRepository).deleteAllByIdInBatch(ids);

        boolean result = teacherTitleService.deleteByIds(ids);

        assertTrue(result);
        verify(teacherTitleRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(teacherRepository, times(1)).unbindTeachersFromTeacherTitle(1);
        verify(teacherRepository, times(1)).unbindTeachersFromTeacherTitle(2);
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(teacherTitleRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, teacherTitleService.lastPageNumber());

        verify(teacherTitleRepository, times(1)).count();
    }

}
