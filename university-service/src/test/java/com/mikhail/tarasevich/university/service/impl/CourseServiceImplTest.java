package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.mapper.CourseMapper;
import com.mikhail.tarasevich.university.repository.CourseRepository;
import com.mikhail.tarasevich.university.repository.DepartmentRepository;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.service.validator.CourseValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.mikhail.tarasevich.university.service.impl.AbstractPageableService.ITEMS_PER_PAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    CourseServiceImpl courseService;
    @Mock
    CourseRepository courseRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    LessonRepository lessonRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    CourseMapper mapper;
    @Mock
    CourseValidator validator;

    private static final Course COURSE_ENTITY_1 = Course.builder()
            .withName("name1")
            .withDescription("description1")
            .build();
    private static final Course COURSE_ENTITY_WITH_ID_1 = Course.builder()
            .withId(1)
            .withName("name1")
            .withDescription("description1")
            .build();
    private static final CourseRequest COURSE_REQUEST_1 = new CourseRequest();
    private static final CourseResponse COURSE_RESPONSE_WITH_ID_1 = new CourseResponse();

    private static final Course COURSE_ENTITY_2 = Course.builder()
            .withName("name2")
            .withDescription("description2")
            .build();
    private static final Course COURSE_ENTITY_WITH_ID_2 = Course.builder()
            .withId(2)
            .withName("name2")
            .withDescription("description2")
            .build();
    private static final CourseRequest COURSE_REQUEST_2 = new CourseRequest();
    private static final CourseResponse COURSE_RESPONSE_WITH_ID_2 = new CourseResponse();

    private static final List<Course> courseEntities = new ArrayList<>();
    private static final List<Course> courseEntitiesWithId = new ArrayList<>();
    private static final List<CourseResponse> courseResponses = new ArrayList<>();

    static {
        COURSE_REQUEST_1.setId(0);
        COURSE_REQUEST_1.setName("name1");
        COURSE_REQUEST_1.setDescription("description");

        COURSE_RESPONSE_WITH_ID_1.setId(1);
        COURSE_RESPONSE_WITH_ID_1.setName("name1");
        COURSE_RESPONSE_WITH_ID_1.setDescription("description");

        COURSE_REQUEST_2.setId(0);
        COURSE_REQUEST_2.setName("name2");
        COURSE_REQUEST_2.setDescription("description2");

        COURSE_RESPONSE_WITH_ID_2.setId(2);
        COURSE_RESPONSE_WITH_ID_2.setName("name2");
        COURSE_RESPONSE_WITH_ID_2.setDescription("description");


        courseEntities.add(COURSE_ENTITY_1);
        courseEntities.add(COURSE_ENTITY_2);

        courseEntitiesWithId.add(COURSE_ENTITY_WITH_ID_1);
        courseEntitiesWithId.add(COURSE_ENTITY_WITH_ID_2);

        courseResponses.add(COURSE_RESPONSE_WITH_ID_1);
        courseResponses.add(COURSE_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputCourseRequest_expectedCourseResponseWithId() {
        when(mapper.toEntity(COURSE_REQUEST_1)).thenReturn(COURSE_ENTITY_1);
        when(courseRepository.save(COURSE_ENTITY_1)).thenReturn(COURSE_ENTITY_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(COURSE_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_1);

        CourseResponse courseResponse = courseService.register(COURSE_REQUEST_1);

        assertEquals(COURSE_RESPONSE_WITH_ID_1, courseResponse);
        verify(mapper, times(1)).toEntity(COURSE_REQUEST_1);
        verify(courseRepository, times(1)).save(COURSE_ENTITY_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(COURSE_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(COURSE_REQUEST_1);
    }

    @Test
    void registerAllCourses_inputCourseRequestListWithRepeatableCourse_expectedNothing() {
        final List<CourseRequest> listForRegister = new ArrayList<>();
        listForRegister.add(COURSE_REQUEST_1);
        listForRegister.add(COURSE_REQUEST_1);
        listForRegister.add(COURSE_REQUEST_2);

        final List<Course> courseEntities = new ArrayList<>();
        courseEntities.add(COURSE_ENTITY_1);
        courseEntities.add(COURSE_ENTITY_2);

        when(mapper.toEntity(COURSE_REQUEST_1)).thenReturn(COURSE_ENTITY_1);
        when(mapper.toEntity(COURSE_REQUEST_2)).thenReturn(COURSE_ENTITY_2);
        doNothing().doThrow(new IncorrectRequestDataException()).when(validator).validateUniqueNameInDB(COURSE_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(COURSE_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_2);

        courseService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(COURSE_REQUEST_1);
        verify(mapper, times(1)).toEntity(COURSE_REQUEST_2);
        verify(courseRepository, times(1)).saveAll(courseEntities);
        verify(validator, times(2)).validateUniqueNameInDB(COURSE_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(COURSE_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundCourse() {
        when(courseRepository.findById(1)).thenReturn(Optional.of(COURSE_ENTITY_WITH_ID_1));
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);

        CourseResponse courseResponse = courseService.findById(1);

        assertEquals(COURSE_RESPONSE_WITH_ID_1, courseResponse);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(courseRepository, times(1)).findById(1);
    }

    @Test
    void findById_inputIncorrectId_expectedException() {
        when(courseRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> courseService.findById(100));

        verify(courseRepository, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllCourses() {
        when(courseRepository.findAll()).thenReturn(courseEntitiesWithId);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll();

        assertEquals(courseResponses, foundCourses);
        verify(courseRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundCoursesFromPageOne() {
        Page<Course> pageOfCourseEntitiesWithId = new PageImpl<>(courseEntitiesWithId);

        when(courseRepository.findAll(PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("id")))).thenReturn(pageOfCourseEntitiesWithId);
        when(courseRepository.count()).thenReturn(2L);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("1");

        assertEquals(courseResponses, foundCourses);
        verify(courseRepository, times(1)).findAll(PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("id")));
        verify(courseRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageMinusOne_expectedFoundCoursesFromDefaultPage() {
        Page<Course> pageOfCourseEntitiesWithId = new PageImpl<>(courseEntitiesWithId);

        when(courseRepository.findAll(PageRequest.of(1, ITEMS_PER_PAGE, Sort.by("id")))).thenReturn(pageOfCourseEntitiesWithId);
        when(courseRepository.count()).thenReturn(2L);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("-1");

        assertEquals(courseResponses, foundCourses);
        verify(courseRepository, times(1)).findAll(PageRequest.of(1, ITEMS_PER_PAGE, Sort.by("id")));
        verify(courseRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageTen_expectedFoundCoursesFromLastPage() {
        Page<Course> pageOfCourseEntitiesWithId = new PageImpl<>(courseEntitiesWithId);

        when(courseRepository.findAll(PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("id")))).thenReturn(pageOfCourseEntitiesWithId);
        when(courseRepository.count()).thenReturn(2L);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("10");

        assertEquals(courseResponses, foundCourses);
        verify(courseRepository, times(1)).findAll(PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("id")));
        verify(courseRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputIncorrectPage_expectedFoundCoursesFromDefaultPage() {
        Page<Course> pageOfCourseEntitiesWithId = new PageImpl<>(courseEntitiesWithId);

        when(courseRepository.findAll(PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("id")))).thenReturn(pageOfCourseEntitiesWithId);
        when(courseRepository.count()).thenReturn(2L);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> foundCourses = courseService.findAll("hello");

        assertEquals(courseResponses, foundCourses);
        verify(courseRepository, times(1)).findAll(PageRequest.of(0, ITEMS_PER_PAGE, Sort.by("id")));
        verify(courseRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findCoursesRelateToDepartmentNotRelateToTeacher_inputDepartmentIdTeacherId_expectedFoundCourses() {
        List<CourseResponse> expected = new ArrayList<>();
        expected.add(COURSE_RESPONSE_WITH_ID_1);

        List<Course> coursesRelateToTeacher = new ArrayList<>();
        coursesRelateToTeacher.add(COURSE_ENTITY_WITH_ID_2);

        when(courseRepository.findCoursesByDepartmentsId(1)).thenReturn(courseEntitiesWithId);
        when(courseRepository.findCoursesByTeachersId(1)).thenReturn(coursesRelateToTeacher);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);

        List<CourseResponse> foundCourses = courseService.findCoursesRelateToDepartmentNotRelateToTeacher(1, 1);

        assertEquals(expected, foundCourses);
        verify(courseRepository, times(1)).findCoursesByDepartmentsId(1);
        verify(courseRepository, times(1)).findCoursesByTeachersId(1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
    }

    @Test
    void edit_inputCourseRequest_expectedNothing() {
        final Course COURSE_ENTITY_FOR_UPDATE_1 = Course.builder()
                .withId(1)
                .withName("update1")
                .withDescription("update1")
                .build();
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_1 = new CourseRequest();
        COURSE_REQUEST_FOR_UPDATE_1.setId(1);
        COURSE_REQUEST_FOR_UPDATE_1.setName("update1");
        COURSE_REQUEST_FOR_UPDATE_1.setDescription("update1");

        doNothing().when(courseRepository).update(COURSE_REQUEST_FOR_UPDATE_1.getId(),
                COURSE_REQUEST_FOR_UPDATE_1.getName(), COURSE_REQUEST_FOR_UPDATE_1.getDescription());
        when(mapper.toEntity(COURSE_REQUEST_FOR_UPDATE_1)).thenReturn(COURSE_ENTITY_FOR_UPDATE_1);

        courseService.edit(COURSE_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(COURSE_REQUEST_FOR_UPDATE_1);
        verify(courseRepository, times(1)).update(COURSE_REQUEST_FOR_UPDATE_1.getId(),
                COURSE_REQUEST_FOR_UPDATE_1.getName(), COURSE_REQUEST_FOR_UPDATE_1.getDescription());
    }

    @Test
    void editAll_inputCourseRequestListWhereOneCourseHasIncorrectName_expectedNothing() {
        final Course COURSE_ENTITY_FOR_UPDATE_1 = Course.builder()
                .withId(1)
                .withName("update1")
                .withDescription("update1")
                .build();
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_1 = new CourseRequest();
        COURSE_REQUEST_FOR_UPDATE_1.setId(1);
        COURSE_REQUEST_FOR_UPDATE_1.setName("update1");
        COURSE_REQUEST_FOR_UPDATE_1.setDescription("update1");

        final Course COURSE_ENTITY_FOR_UPDATE_2 = Course.builder()
                .withId(2)
                .withName("update2")
                .withDescription("update2")
                .build();
        final CourseRequest COURSE_REQUEST_FOR_UPDATE_2 = new CourseRequest();
        COURSE_REQUEST_FOR_UPDATE_2.setId(2);
        COURSE_REQUEST_FOR_UPDATE_2.setName("update2");
        COURSE_REQUEST_FOR_UPDATE_2.setDescription("update2");

        final CourseRequest COURSE_REQUEST_FOR_UPDATE_INCORRECT = new CourseRequest();
        COURSE_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        COURSE_REQUEST_FOR_UPDATE_INCORRECT.setName("   ");
        COURSE_REQUEST_FOR_UPDATE_INCORRECT.setDescription("update3");

        final List<CourseRequest> inputList = new ArrayList<>();
        inputList.add(COURSE_REQUEST_FOR_UPDATE_1);
        inputList.add(COURSE_REQUEST_FOR_UPDATE_2);
        inputList.add(COURSE_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Course> listForUpdate = new ArrayList<>();
        listForUpdate.add(COURSE_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(COURSE_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(COURSE_REQUEST_FOR_UPDATE_1)).thenReturn(COURSE_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(COURSE_REQUEST_FOR_UPDATE_2)).thenReturn(COURSE_ENTITY_FOR_UPDATE_2);

        courseService.editAll(inputList);

        verify(mapper, times(1)).toEntity(COURSE_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(COURSE_REQUEST_FOR_UPDATE_2);
        verify(courseRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(COURSE_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputCourseId_expectedSuccessDelete() {
        int id = 1;

        when(courseRepository.findById(id)).thenReturn(Optional.of(COURSE_ENTITY_1));
        doNothing().when(departmentRepository).unbindDepartmentsFromCourse(id);
        doNothing().when(lessonRepository).unbindLessonsFromCourse(id);
        doNothing().when(teacherRepository).unbindTeachersFromCourse(id);
        doNothing().when(courseRepository).deleteById(id);

        boolean result = courseService.deleteById(1);

        assertTrue(result);
        verify(courseRepository, times(1)).findById(id);
        verify(courseRepository, times(1)).deleteById(id);
        verify(departmentRepository, times(1)).unbindDepartmentsFromCourse(id);
        verify(lessonRepository, times(1)).unbindLessonsFromCourse(id);
        verify(teacherRepository, times(1)).unbindTeachersFromCourse(id);
    }

    @Test
    void deleteById_inputCourseId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = courseService.deleteById(1);

        assertFalse(result);
        verify(courseRepository, times(1)).findById(id);
        verifyNoInteractions(departmentRepository);
        verifyNoInteractions(teacherRepository);
        verifyNoInteractions(lessonRepository);
        verify(courseRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputCoursesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(departmentRepository).unbindDepartmentsFromCourse(1);
        doNothing().when(lessonRepository).unbindLessonsFromCourse(1);
        doNothing().when(teacherRepository).unbindTeachersFromCourse(1);
        doNothing().when(departmentRepository).unbindDepartmentsFromCourse(2);
        doNothing().when(lessonRepository).unbindLessonsFromCourse(2);
        doNothing().when(teacherRepository).unbindTeachersFromCourse(2);
        doNothing().when(courseRepository).deleteAllByIdInBatch(ids);

        boolean result = courseService.deleteByIds(ids);

        assertTrue(result);
        verify(courseRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(departmentRepository, times(1)).unbindDepartmentsFromCourse(1);
        verify(lessonRepository, times(1)).unbindLessonsFromCourse(1);
        verify(teacherRepository, times(1)).unbindTeachersFromCourse(1);
        verify(departmentRepository, times(1)).unbindDepartmentsFromCourse(2);
        verify(lessonRepository, times(1)).unbindLessonsFromCourse(2);
        verify(teacherRepository, times(1)).unbindTeachersFromCourse(2);
    }

    @Test
    void findCoursesRelateToDepartment_inputDepartmentId_expectedCoursesList() {
        int departmentId = 1;

        when(courseRepository.findCoursesByDepartmentsId(departmentId)).thenReturn(courseEntitiesWithId);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> courses = courseService.findCoursesRelateToDepartment(departmentId);

        assertEquals(courseResponses, courses);
        verify(courseRepository, times(1)).findCoursesByDepartmentsId(departmentId);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void findCoursesNotRelateToDepartment_inputDepartmentId_expectedCoursesList() {
        List<Course> coursesRelateToDepartment = new ArrayList<>();
        coursesRelateToDepartment.add(COURSE_ENTITY_WITH_ID_2);

        List<CourseResponse> expected = new ArrayList<>();
        expected.add(COURSE_RESPONSE_WITH_ID_1);

        int departmentId = 1;

        when(courseRepository.findAll()).thenReturn(courseEntitiesWithId);
        when(courseRepository.findCoursesByDepartmentsId(departmentId)).thenReturn(coursesRelateToDepartment);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);

        List<CourseResponse> courses = courseService.findCoursesNotRelateToDepartment(departmentId);

        assertEquals(expected, courses);
        verify(courseRepository, times(1)).findAll();
        verify(courseRepository, times(1)).findCoursesByDepartmentsId(departmentId);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
    }

    @Test
    void findCoursesRelateToTeacher_inputTeacherId_expectedCoursesList() {
        int teacherId = 1;

        when(courseRepository.findCoursesByTeachersId(teacherId)).thenReturn(courseEntitiesWithId);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_1)).thenReturn(COURSE_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(COURSE_ENTITY_WITH_ID_2)).thenReturn(COURSE_RESPONSE_WITH_ID_2);

        List<CourseResponse> courses = courseService.findCoursesRelateToTeacher(teacherId);

        assertEquals(courseResponses, courses);
        verify(courseRepository, times(1)).findCoursesByTeachersId(teacherId);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(COURSE_ENTITY_WITH_ID_2);
    }

    @Test
    void subscribeCourseToDepartment_inputCourseIdDepartmentId_expectedNothing() {
        doNothing().when(departmentRepository).addCourseToDepartment(1, 1);

        courseService.subscribeCourseToDepartment(1, 1);

        verify(departmentRepository, times(1)).addCourseToDepartment(1, 1);
    }

    @Test
    void unsubscribeCourseToDepartment_inputCourseIdDepartmentId_expectedNothing() {
        doNothing().when(departmentRepository).deleteCourseFromDepartment(1, 1);

        courseService.unsubscribeCourseFromDepartment(1, 1);

        verify(departmentRepository, times(1)).deleteCourseFromDepartment(1, 1);
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(courseRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / ITEMS_PER_PAGE);

        assertEquals(expected, courseService.lastPageNumber());

        verify(courseRepository, times(1)).count();
    }

}
