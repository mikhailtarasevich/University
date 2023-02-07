package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.CourseMapper;
import com.mikhail.tarasevich.university.validator.CourseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    CourseServiceImpl courseService;
    @Mock
    CourseDao courseDao;
    @Mock
    DepartmentDao departmentDao;
    @Mock
    LessonDao lessonDao;
    @Mock
    TeacherDao teacherDao;
    @Mock
    CourseMapper courseMapper;
    @Mock
    CourseValidator validator;

    private static final Course COURSE_ENTITY_1 =
            Course.builder().withName("name1").withDescription("description1").build();
    private static final Course COURSE_ENTITY_WITH_ID_1 =
            Course.builder().withId(1).withName("name1").withDescription("description1").build();
    private static final CourseRequest COURSE_REQUEST_1 =
            new CourseRequest(0, "name1", "description1");
    private static final CourseResponse COURSE_RESPONSE_WITH_ID_1 =
            new CourseResponse(1, "name1", "description1");

    private static final Course COURSE_ENTITY_2 =
            Course.builder().withName("name2").withDescription("description2").build();
    private static final Course COURSE_ENTITY_WITH_ID_2 =
            Course.builder().withId(2).withName("name2").withDescription("description2").build();
    private static final CourseRequest COURSE_REQUEST_2 =
            new CourseRequest(0, "name2", "description2");
    private static final CourseResponse COURSE_RESPONSE_WITH_ID_2 =
            new CourseResponse(2, "name2", "description2");

    private final List<Course> courseEntities = new ArrayList<>();
    private final List<Course> courseEntitiesWithId = new ArrayList<>();
    private final List<CourseResponse> courseResponses = new ArrayList<>();

    {
        courseEntities.add(COURSE_ENTITY_1);
        courseEntities.add(COURSE_ENTITY_2);

        courseEntitiesWithId.add(COURSE_ENTITY_WITH_ID_1);
        courseEntitiesWithId.add(COURSE_ENTITY_WITH_ID_2);

        courseResponses.add(COURSE_RESPONSE_WITH_ID_1);
        courseResponses.add(COURSE_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputCourseRequest_expectedCourseResponseWithId() {
        when(courseMapper.toEntity(COURSE_REQUEST_1)).thenReturn(COURSE_ENTITY_1);
        when(courseDao.save(COURSE_ENTITY_1)).thenReturn(COURSE_ENTITY_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(COURSE_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_1);

        CourseResponse courseResponse = courseService.register(COURSE_REQUEST_1);

        assertEquals(COURSE_RESPONSE_WITH_ID_1, courseResponse);
        verify(courseMapper, times(1)).toEntity(COURSE_REQUEST_1);
        verify(courseDao, times(1)).save(COURSE_ENTITY_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(COURSE_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(COURSE_REQUEST_1);
    }

    @Test
    void registerAllCourses_inputCourseRequestListWithRepeatableCourse_expectedNothing() {
        final List<CourseRequest> listForRegister = new ArrayList<>();
        listForRegister.add(COURSE_REQUEST_1);
        listForRegister.add(COURSE_REQUEST_1);
        listForRegister.add(COURSE_REQUEST_2);

        when(courseMapper.toEntity(COURSE_REQUEST_1)).thenReturn(COURSE_ENTITY_1);
        when(courseMapper.toEntity(COURSE_REQUEST_2)).thenReturn(COURSE_ENTITY_2);
        doNothing().when(courseDao).saveAll(courseEntities);
        doNothing().doThrow(new IncorrectRequestData()).when(validator).validateUniqueNameInDB(COURSE_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(COURSE_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_2);

        courseService.registerAll(listForRegister);

        verify(courseMapper, times(1)).toEntity(COURSE_REQUEST_1);
        verify(courseMapper, times(1)).toEntity(COURSE_REQUEST_2);
        verify(courseDao, times(1)).saveAll(courseEntities);
        verify(validator, times(2)).validateUniqueNameInDB(COURSE_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(COURSE_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundCourse() {
        when(courseDao.findById(1)).thenReturn(Optional.of(COURSE_ENTITY_WITH_ID_1));
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);

        Optional<CourseResponse> courseResponse = courseService.findById(1);

        assertEquals(Optional.of(COURSE_RESPONSE_WITH_ID_1), courseResponse);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundCoursesFromPageOne() {
        when(courseDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(courseEntitiesWithId);
        when(courseDao.count()).thenReturn(2L);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("1");

        assertEquals(courseResponses, foundCourses);
        verify(courseDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(courseDao, times(1)).count();
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageMinusOne_expectedFoundCoursesFromDefaultPage() {
        when(courseDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(courseEntitiesWithId);
        when(courseDao.count()).thenReturn(2L);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("-1");

        assertEquals(courseResponses, foundCourses);
        verify(courseDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(courseDao, times(1)).count();
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageTen_expectedFoundCoursesFromLastPage() {
        when(courseDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(courseEntitiesWithId);
        when(courseDao.count()).thenReturn(2L);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("10");

        assertEquals(courseResponses, foundCourses);
        verify(courseDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(courseDao, times(1)).count();
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputIncorrectPage_expectedFoundCoursesFromDefaultPage() {
        when(courseDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE)).thenReturn(courseEntitiesWithId);
        when(courseDao.count()).thenReturn(2L);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("hello");

        assertEquals(courseResponses, foundCourses);
        verify(courseDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(courseDao, times(1)).count();
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputCourseRequest_expectedNothing() {
        final Course COURSE_ENTITY_FOR_UPDATE_1 =
                Course.builder().withId(1).withName("update1").withDescription("update1").build();
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_1 =
                new CourseRequest(1, "update1", "update1");

        doNothing().when(courseDao).update(COURSE_ENTITY_FOR_UPDATE_1);
        when(courseMapper.toEntity(COURSE_REQUEST_FOR_UPDATE_1)).thenReturn(COURSE_ENTITY_FOR_UPDATE_1);

        courseService.edit(COURSE_REQUEST_FOR_UPDATE_1);

        verify(courseMapper, times(1)).toEntity(COURSE_REQUEST_FOR_UPDATE_1);
        verify(courseDao, times(1)).update(COURSE_ENTITY_FOR_UPDATE_1);
    }

    @Test
    void editAll_inputCourseRequestListWhereOneCourseHasIncorrectName_expectedNothing() {
        final Course COURSE_ENTITY_FOR_UPDATE_1 =
                Course.builder().withId(1).withName("update1").withDescription("update1").build();
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_1 =
                new CourseRequest(1, "update1", "update1");
        final Course COURSE_ENTITY_FOR_UPDATE_2 =
                Course.builder().withId(2).withName("update2").withDescription("update2").build();
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_2 =
                new CourseRequest(2, "update2", "update2");
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_INCORRECT =
                new CourseRequest(3, " ", "update3");

        final List<CourseRequest> inputList = new ArrayList<>();
        inputList.add(COURSE_REQUEST_FOR_UPDATE_1);
        inputList.add(COURSE_REQUEST_FOR_UPDATE_2);
        inputList.add(COURSE_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Course> listForUpdate = new ArrayList<>();
        listForUpdate.add(COURSE_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(COURSE_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData()).when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_INCORRECT);

        when(courseMapper.toEntity(COURSE_REQUEST_FOR_UPDATE_1)).thenReturn(COURSE_ENTITY_FOR_UPDATE_1);
        when(courseMapper.toEntity(COURSE_REQUEST_FOR_UPDATE_2)).thenReturn(COURSE_ENTITY_FOR_UPDATE_2);

        doNothing().when(courseDao).updateAll(listForUpdate);

        courseService.editAll(inputList);

        verify(courseMapper, times(1)).toEntity(COURSE_REQUEST_FOR_UPDATE_1);
        verify(courseMapper, times(1)).toEntity(COURSE_REQUEST_FOR_UPDATE_2);
        verify(courseDao, times(1)).updateAll(listForUpdate);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputCourseId_expectedSuccessDelete() {
        int id = 1;

        when(courseDao.findById(id)).thenReturn(Optional.of(COURSE_ENTITY_1));
        doNothing().when(departmentDao).unbindDepartmentsFromCourse(id);
        doNothing().when(lessonDao).unbindLessonsFromCourse(id);
        doNothing().when(teacherDao).unbindTeachersFromCourse(id);
        when(courseDao.deleteById(id)).thenReturn(true);

        boolean result = courseService.deleteById(1);

        assertTrue(result);
        verify(courseDao, times(1)).findById(id);
        verify(courseDao, times(1)).deleteById(id);
        verify(departmentDao, times(1)).unbindDepartmentsFromCourse(id);
        verify(lessonDao, times(1)).unbindLessonsFromCourse(id);
        verify(teacherDao, times(1)).unbindTeachersFromCourse(id);
    }

    @Test
    void deleteById_inputCourseId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(courseDao.findById(id)).thenReturn(Optional.empty());

        boolean result = courseService.deleteById(1);

        assertFalse(result);
        verify(courseDao, times(1)).findById(id);
        verifyNoInteractions(departmentDao);
        verifyNoInteractions(teacherDao);
        verifyNoInteractions(lessonDao);
        verify(courseDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputCoursesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(departmentDao).unbindDepartmentsFromCourse(1);
        doNothing().when(lessonDao).unbindLessonsFromCourse(1);
        doNothing().when(teacherDao).unbindTeachersFromCourse(1);
        doNothing().when(departmentDao).unbindDepartmentsFromCourse(2);
        doNothing().when(lessonDao).unbindLessonsFromCourse(2);
        doNothing().when(teacherDao).unbindTeachersFromCourse(2);
        when(courseDao.deleteByIds(ids)).thenReturn(true);

        boolean result = courseService.deleteByIds(ids);

        assertTrue(result);
        verify(courseDao, times(1)).deleteByIds(ids);
        verify(departmentDao, times(1)).unbindDepartmentsFromCourse(1);
        verify(lessonDao, times(1)).unbindLessonsFromCourse(1);
        verify(teacherDao, times(1)).unbindTeachersFromCourse(1);
        verify(departmentDao, times(1)).unbindDepartmentsFromCourse(2);
        verify(lessonDao, times(1)).unbindLessonsFromCourse(2);
        verify(teacherDao, times(1)).unbindTeachersFromCourse(2);
    }

    @Test
    void findCoursesRelateToDepartment_inputDepartmentId_expectedCoursesList() {
        int departmentId = 1;

        when(courseDao.findCoursesRelateToDepartment(departmentId)).thenReturn(courseEntitiesWithId);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> courses = courseService.findCoursesRelateToDepartment(departmentId);

        assertEquals(courseResponses, courses);
        verify(courseDao, times(1)).findCoursesRelateToDepartment(departmentId);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findCoursesRelateToTeacher_inputTeacherId_expectedCoursesList() {
        int teacherId = 1;

        when(courseDao.findCoursesRelateToTeacher(teacherId)).thenReturn(courseEntitiesWithId);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(courseMapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> courses = courseService.findCoursesRelateToTeacher(teacherId);

        assertEquals(courseResponses, courses);
        verify(courseDao, times(1)).findCoursesRelateToTeacher(teacherId);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseMapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void subscribeCourseToDepartment_inputCourseIdDepartmentId_expectedNothing() {
        doNothing().when(departmentDao).addCourseToDepartment(1, 1);

        courseService.subscribeCourseToDepartment(1, 1);

        verify(departmentDao, times(1)).addCourseToDepartment(1, 1);
    }

    @Test
    void unsubscribeCourseToDepartment_inputCourseIdDepartmentId_expectedNothing() {
        doNothing().when(departmentDao).deleteCourseFromDepartment(1, 1);

        courseService.unsubscribeCourseFromDepartment(1, 1);

        verify(departmentDao, times(1)).deleteCourseFromDepartment(1, 1);
    }

}
