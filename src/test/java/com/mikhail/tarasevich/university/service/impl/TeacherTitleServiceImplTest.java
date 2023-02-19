package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.dto.TeacherTitleRequest;
import com.mikhail.tarasevich.university.dto.TeacherTitleResponse;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.TeacherTitleMapper;
import com.mikhail.tarasevich.university.validator.TeacherTitleValidator;
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
    TeacherTitleDao teacherTitleDao;
    @Mock
    TeacherDao teacherDao;
    @Mock
    TeacherTitleMapper teacherTitleMapper;
    @Mock
    TeacherTitleValidator teacherTitleValidator;

    private static final TeacherTitle TEACHER_TITLE_ENTITY_1 =
            TeacherTitle.builder().withName("name1").build();
    private static final TeacherTitle TEACHER_TITLE_ENTITY_WITH_ID_1 =
            TeacherTitle.builder().withId(1).withName("name1").build();
    private static final TeacherTitleRequest TEACHER_TITLE_REQUEST_1 =
            new TeacherTitleRequest(0, "name1");
    private static final TeacherTitleResponse TEACHER_TITLE_WITH_ID_1 =
            new TeacherTitleResponse(1, "name1");

    private static final TeacherTitle TEACHER_TITLE_ENTITY_2 =
            TeacherTitle.builder().withName("name2").build();
    private static final TeacherTitle TEACHER_TITLE_ENTITY_WITH_ID_2 =
            TeacherTitle.builder().withId(2).withName("name2").build();
    private static final TeacherTitleRequest TEACHER_TITLE_REQUEST_2 =
            new TeacherTitleRequest(0, "name2");
    private static final TeacherTitleResponse TEACHER_TITLE_WITH_ID_2 =
            new TeacherTitleResponse(2, "name2");

    private final List<TeacherTitle> teacherTitleEntities = new ArrayList<>();
    private final List<TeacherTitle> teacherTitleEntitiesWithId = new ArrayList<>();
    private final List<TeacherTitleResponse> teacherTitleResponses = new ArrayList<>();

    {
        teacherTitleEntities.add(TEACHER_TITLE_ENTITY_1);
        teacherTitleEntities.add(TEACHER_TITLE_ENTITY_2);

        teacherTitleEntitiesWithId.add(TEACHER_TITLE_ENTITY_WITH_ID_1);
        teacherTitleEntitiesWithId.add(TEACHER_TITLE_ENTITY_WITH_ID_2);

        teacherTitleResponses.add(TEACHER_TITLE_WITH_ID_1);
        teacherTitleResponses.add(TEACHER_TITLE_WITH_ID_2);
    }

    @Test
    void register_inputTeacherTitleRequest_expectedTeacherTitleResponseWithId() {
        when(teacherTitleMapper.toEntity(TEACHER_TITLE_REQUEST_1)).thenReturn(TEACHER_TITLE_ENTITY_1);
        when(teacherTitleDao.save(TEACHER_TITLE_ENTITY_1)).thenReturn(TEACHER_TITLE_ENTITY_WITH_ID_1);
        when(teacherTitleMapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);
        doNothing().when(teacherTitleValidator).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        doNothing().when(teacherTitleValidator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_1);

        TeacherTitleResponse teacherTitleResponse = teacherTitleService.register(TEACHER_TITLE_REQUEST_1);

        assertEquals(TEACHER_TITLE_WITH_ID_1, teacherTitleResponse);
        verify(teacherTitleMapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_1);
        verify(teacherTitleDao, times(1)).save(TEACHER_TITLE_ENTITY_1);
        verify(teacherTitleMapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
        verify(teacherTitleValidator, times(1)).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        verify(teacherTitleValidator, times(1)).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
    }

    @Test
    void registerAllCourses_inputTeacherTitleRequestListWithRepeatableCourse_expectedNothing() {
        final List<TeacherTitleRequest> listForRegister = new ArrayList<>();
        listForRegister.add(TEACHER_TITLE_REQUEST_1);
        listForRegister.add(TEACHER_TITLE_REQUEST_1);
        listForRegister.add(TEACHER_TITLE_REQUEST_2);

        when(teacherTitleMapper.toEntity(TEACHER_TITLE_REQUEST_1)).thenReturn(TEACHER_TITLE_ENTITY_1);
        when(teacherTitleMapper.toEntity(TEACHER_TITLE_REQUEST_2)).thenReturn(TEACHER_TITLE_ENTITY_2);
        doNothing().when(teacherTitleDao).saveAll(teacherTitleEntities);
        doNothing().doThrow(new IncorrectRequestData()).when(teacherTitleValidator).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        doNothing().when(teacherTitleValidator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_1);
        doNothing().when(teacherTitleValidator).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_2);
        doNothing().when(teacherTitleValidator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_2);

        teacherTitleService.registerAll(listForRegister);

        verify(teacherTitleMapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_1);
        verify(teacherTitleMapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_2);
        verify(teacherTitleDao, times(1)).saveAll(teacherTitleEntities);
        verify(teacherTitleValidator, times(2)).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_1);
        verify(teacherTitleValidator, times(1)).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_1);
        verify(teacherTitleValidator, times(1)).validateUniqueNameInDB(TEACHER_TITLE_REQUEST_2);
        verify(teacherTitleValidator, times(1)).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundTeacherTitle() {
        when(teacherTitleDao.findById(1)).thenReturn(Optional.of(TEACHER_TITLE_ENTITY_WITH_ID_1));
        when(teacherTitleMapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);

        Optional<TeacherTitleResponse> teacherTitleResponse = teacherTitleService.findById(1);

        assertEquals(Optional.of(TEACHER_TITLE_WITH_ID_1), teacherTitleResponse);
        verify(teacherTitleMapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
        verify(teacherTitleDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundTeacherTitlesFromPageOne() {
        when(teacherTitleDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(teacherTitleEntitiesWithId);
        when(teacherTitleDao.count()).thenReturn(2L);
        when(teacherTitleMapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1)).thenReturn(TEACHER_TITLE_WITH_ID_1);
        when(teacherTitleMapper.toResponse(TEACHER_TITLE_ENTITY_WITH_ID_2)).thenReturn(TEACHER_TITLE_WITH_ID_2);

        List<TeacherTitleResponse> foundTeacherTitles = teacherTitleService.findAll("1");

        assertEquals(teacherTitleResponses, foundTeacherTitles);
        verify(teacherTitleDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(teacherTitleDao, times(1)).count();
        verify(teacherTitleMapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_1);
        verify(teacherTitleMapper, times(1)).toResponse(TEACHER_TITLE_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputTeacherTitleRequest_expectedNothing() {
        final TeacherTitle TEACHER_TITLE_ENTITY_FOR_UPDATE_1 =
                TeacherTitle.builder().withId(1).withName("update1").build();
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_1 =
                new TeacherTitleRequest(1, "update1");

        doNothing().when(teacherTitleDao).update(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
        when(teacherTitleMapper.toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);

        teacherTitleService.edit(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);

        verify(teacherTitleMapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        verify(teacherTitleDao, times(1)).update(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputTeacherTitleRequestListWhereOneTeacherTitleHasIncorrectName_expectedNothing() {
        final TeacherTitle TEACHER_TITLE_ENTITY_FOR_UPDATE_1 =
                TeacherTitle.builder().withId(1).withName("update1").build();
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_1 =
                new TeacherTitleRequest(1, "update1");
        final TeacherTitle TEACHER_TITLE_ENTITY_FOR_UPDATE_2 =
                TeacherTitle.builder().withId(2).withName("update2").build();
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_2 =
                new TeacherTitleRequest(2, "update2");
        final TeacherTitleRequest TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT =
                new TeacherTitleRequest(3, " ");

        final List<TeacherTitleRequest> inputList = new ArrayList<>();
        inputList.add(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        inputList.add(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        inputList.add(TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT);

        final List<TeacherTitle> listForUpdate = new ArrayList<>();
        listForUpdate.add(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(TEACHER_TITLE_ENTITY_FOR_UPDATE_2);

        doNothing().when(teacherTitleValidator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        doNothing().when(teacherTitleValidator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData()).when(teacherTitleValidator).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT);

        when(teacherTitleMapper.toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1)).thenReturn(TEACHER_TITLE_ENTITY_FOR_UPDATE_1);
        when(teacherTitleMapper.toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_2)).thenReturn(TEACHER_TITLE_ENTITY_FOR_UPDATE_2);

        doNothing().when(teacherTitleDao).updateAll(listForUpdate);

        teacherTitleService.editAll(inputList);

        verify(teacherTitleMapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        verify(teacherTitleMapper, times(1)).toEntity(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        verify(teacherTitleDao, times(1)).updateAll(listForUpdate);
        verify(teacherTitleValidator, times(1)).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_1);
        verify(teacherTitleValidator, times(1)).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_2);
        verify(teacherTitleValidator, times(1)).validateNameNotNullOrEmpty(TEACHER_TITLE_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputTeacherTitleId_expectedSuccessDelete() {
        int id = 1;

        when(teacherTitleDao.findById(id)).thenReturn(Optional.of(TEACHER_TITLE_ENTITY_1));
        doNothing().when(teacherDao).unbindTeachersFromTeacherTitle(id);
        when(teacherTitleDao.deleteById(id)).thenReturn(true);

        boolean result = teacherTitleService.deleteById(1);

        assertTrue(result);
        verify(teacherTitleDao, times(1)).findById(id);
        verify(teacherTitleDao, times(1)).deleteById(id);
        verify(teacherDao, times(1)).unbindTeachersFromTeacherTitle(id);
    }

    @Test
    void deleteById_inputTeacherTitleId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(teacherTitleDao.findById(id)).thenReturn(Optional.empty());

        boolean result = teacherTitleService.deleteById(1);

        assertFalse(result);
        verify(teacherTitleDao, times(1)).findById(id);
        verifyNoInteractions(teacherDao);
        verify(teacherTitleDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputCoursesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(teacherDao).unbindTeachersFromTeacherTitle(1);
        doNothing().when(teacherDao).unbindTeachersFromTeacherTitle(2);
        when(teacherTitleDao.deleteByIds(ids)).thenReturn(true);

        boolean result = teacherTitleService.deleteByIds(ids);

        assertTrue(result);
        verify(teacherTitleDao, times(1)).deleteByIds(ids);
        verify(teacherDao, times(1)).unbindTeachersFromTeacherTitle(1);
        verify(teacherDao, times(1)).unbindTeachersFromTeacherTitle(2);
    }

}
