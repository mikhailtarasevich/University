package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.mapper.DepartmentMapper;
import com.mikhail.tarasevich.university.repository.CourseRepository;
import com.mikhail.tarasevich.university.repository.DepartmentRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.service.validator.DepartmentValidator;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @InjectMocks
    DepartmentServiceImpl departmentService;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    CourseRepository courseRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    DepartmentMapper mapper;
    @Mock
    DepartmentValidator validator;

    private static final Department DEPARTMENT_ENTITY_1 = Department.builder()
            .withName("name1")
            .withDescription("description1")
            .build();
    private static final Department DEPARTMENT_ENTITY_WITH_ID_1 = Department.builder()
            .withId(1)
            .withName("name1")
            .withDescription("description1")
            .build();
    private static final DepartmentRequest DEPARTMENT_REQUEST_1 = new DepartmentRequest();
    private static final DepartmentResponse DEPARTMENT_RESPONSE_WITH_ID_1 = new DepartmentResponse();

    private static final Department DEPARTMENT_ENTITY_2 = Department.builder()
            .withName("name2")
            .withDescription("description2")
            .build();
    private static final Department DEPARTMENT_ENTITY_WITH_ID_2 = Department.builder()
            .withId(2)
            .withName("name2")
            .withDescription("description2")
            .build();
    private static final DepartmentRequest DEPARTMENT_REQUEST_2 = new DepartmentRequest();
    private static final DepartmentResponse DEPARTMENT_RESPONSE_WITH_ID_2 = new DepartmentResponse();


    private static final List<Department> departmentEntities = new ArrayList<>();
    private static final List<Department> departmentEntitiesWithId = new ArrayList<>();
    private static final List<DepartmentResponse> departmentResponses = new ArrayList<>();

    static {
        DEPARTMENT_REQUEST_1.setId(0);
        DEPARTMENT_REQUEST_1.setName("name1");
        DEPARTMENT_REQUEST_1.setDescription("description1");

        DEPARTMENT_RESPONSE_WITH_ID_1.setId(1);
        DEPARTMENT_RESPONSE_WITH_ID_1.setName("name1");
        DEPARTMENT_RESPONSE_WITH_ID_1.setDescription("description1");

        DEPARTMENT_REQUEST_2.setId(0);
        DEPARTMENT_REQUEST_2.setName("name2");
        DEPARTMENT_REQUEST_2.setDescription("description2");

        DEPARTMENT_RESPONSE_WITH_ID_2.setId(2);
        DEPARTMENT_RESPONSE_WITH_ID_2.setName("name2");
        DEPARTMENT_RESPONSE_WITH_ID_2.setDescription("description2");


        departmentEntities.add(DEPARTMENT_ENTITY_1);
        departmentEntities.add(DEPARTMENT_ENTITY_2);

        departmentEntitiesWithId.add(DEPARTMENT_ENTITY_WITH_ID_1);
        departmentEntitiesWithId.add(DEPARTMENT_ENTITY_WITH_ID_2);

        departmentResponses.add(DEPARTMENT_RESPONSE_WITH_ID_1);
        departmentResponses.add(DEPARTMENT_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputDepartmentRequest_expectedDepartmentResponseWithId() {
        when(mapper.toEntity(DEPARTMENT_REQUEST_1)).thenReturn(DEPARTMENT_ENTITY_1);
        when(departmentRepository.save(DEPARTMENT_ENTITY_1)).thenReturn(DEPARTMENT_ENTITY_WITH_ID_1);
        when(mapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_1);

        DepartmentResponse departmentResponse = departmentService.register(DEPARTMENT_REQUEST_1);

        assertEquals(DEPARTMENT_RESPONSE_WITH_ID_1, departmentResponse);
        verify(mapper, times(1)).toEntity(DEPARTMENT_REQUEST_1);
        verify(departmentRepository, times(1)).save(DEPARTMENT_ENTITY_1);
        verify(mapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
    }

    @Test
    void registerAllDepartments_inputDepartmentRequestListWithRepeatableDepartment_expectedNothing() {
        final List<DepartmentRequest> listForRegister = new ArrayList<>();
        listForRegister.add(DEPARTMENT_REQUEST_1);
        listForRegister.add(DEPARTMENT_REQUEST_2);
        listForRegister.add(DEPARTMENT_REQUEST_1);

        when(mapper.toEntity(DEPARTMENT_REQUEST_1)).thenReturn(DEPARTMENT_ENTITY_1);
        when(mapper.toEntity(DEPARTMENT_REQUEST_2)).thenReturn(DEPARTMENT_ENTITY_2);
        doNothing().doThrow(new IncorrectRequestDataException()).when(validator)
                .validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(DEPARTMENT_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_2);

        departmentService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(DEPARTMENT_REQUEST_1);
        verify(mapper, times(1)).toEntity(DEPARTMENT_REQUEST_2);
        verify(departmentRepository, times(1)).saveAll(departmentEntities);
        verify(validator, times(2)).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(DEPARTMENT_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundCourse() {
        when(departmentRepository.findById(1)).thenReturn(Optional.of(DEPARTMENT_ENTITY_WITH_ID_1));
        when(mapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);

        DepartmentResponse departmentResponse = departmentService.findById(1);

        assertEquals(DEPARTMENT_RESPONSE_WITH_ID_1, departmentResponse);
        verify(mapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    void findById_inputIncorrectId_expectedException() {
        when(departmentRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> departmentService.findById(100));

        verify(departmentRepository, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllDepartments() {
        when(departmentRepository.findAll()).thenReturn(departmentEntitiesWithId);
        when(mapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_2)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_2);

        List<DepartmentResponse> foundDepartments = departmentService.findAll();

        assertEquals(departmentResponses, foundDepartments);
        verify(departmentRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundDepartmentsFromPageOne() {
        Page<Department> pageOfDepartmentEntitiesWithId = new PageImpl<>(departmentEntitiesWithId);

        when(departmentRepository
                .findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id"))))
                .thenReturn(pageOfDepartmentEntitiesWithId);
        when(departmentRepository.count()).thenReturn(2L);
        when(mapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_2)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_2);

        List<DepartmentResponse> foundDepartments = departmentService.findAll("1");

        assertEquals(departmentResponses, foundDepartments);
        verify(departmentRepository, times(1)).findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id")));
        verify(departmentRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputDepartmentRequest_expectedNothing() {
        final Department DEPARTMENT_ENTITY_FOR_UPDATE_1 = Department.builder()
                .withId(1)
                .withName("update1")
                .withDescription("update1")
                .build();

        final DepartmentRequest DEPARTMENT_REQUEST_FOR_UPDATE_1 = new DepartmentRequest();
        DEPARTMENT_REQUEST_FOR_UPDATE_1.setId(1);
        DEPARTMENT_REQUEST_FOR_UPDATE_1.setName("update1");
        DEPARTMENT_REQUEST_FOR_UPDATE_1.setDescription("update1");

        doNothing().when(departmentRepository).update(DEPARTMENT_ENTITY_FOR_UPDATE_1.getId(),
            DEPARTMENT_ENTITY_FOR_UPDATE_1.getName(), DEPARTMENT_ENTITY_FOR_UPDATE_1.getDescription());
        when(mapper.toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1)).thenReturn(DEPARTMENT_ENTITY_FOR_UPDATE_1);

        departmentService.edit(DEPARTMENT_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        verify(departmentRepository, times(1)).update(DEPARTMENT_ENTITY_FOR_UPDATE_1.getId(),
                DEPARTMENT_ENTITY_FOR_UPDATE_1.getName(), DEPARTMENT_ENTITY_FOR_UPDATE_1.getDescription());
    }

    @Test
    void editAll_inputDepartmentRequestListWhereOneDepartmentHasIncorrectName_expectedNothing() {
        final Department DEPARTMENT_ENTITY_FOR_UPDATE_1 = Department.builder()
                .withId(1)
                .withName("update1")
                .withDescription("update1")
                .build();

        final DepartmentRequest DEPARTMENT_REQUEST_FOR_UPDATE_1 = new DepartmentRequest();
        DEPARTMENT_REQUEST_FOR_UPDATE_1.setId(1);
        DEPARTMENT_REQUEST_FOR_UPDATE_1.setName("update1");
        DEPARTMENT_REQUEST_FOR_UPDATE_1.setDescription("update1");

        final Department DEPARTMENT_ENTITY_FOR_UPDATE_2 = Department.builder()
                .withId(2)
                .withName("update2")
                .withDescription("update2")
                .build();
        final DepartmentRequest DEPARTMENT_REQUEST_FOR_UPDATE_2 = new DepartmentRequest();
        DEPARTMENT_REQUEST_FOR_UPDATE_2.setId(2);
        DEPARTMENT_REQUEST_FOR_UPDATE_2.setName("update2");
        DEPARTMENT_REQUEST_FOR_UPDATE_2.setDescription("update2");

        final DepartmentRequest DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT = new DepartmentRequest();
        DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT.setName("   ");
        DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT.setDescription("update3");

        final List<DepartmentRequest> inputList = new ArrayList<>();
        inputList.add(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        inputList.add(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        inputList.add(DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Department> listForUpdate = new ArrayList<>();
        listForUpdate.add(DEPARTMENT_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(DEPARTMENT_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator)
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1)).thenReturn(DEPARTMENT_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_2)).thenReturn(DEPARTMENT_ENTITY_FOR_UPDATE_2);

        departmentService.editAll(inputList);

        verify(mapper, times(1)).toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        verify(departmentRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputDepartmentId_expectedSuccessDelete() {
        int id = 1;

        when(departmentRepository.findById(id)).thenReturn(Optional.of(DEPARTMENT_ENTITY_1));
        doNothing().when(courseRepository).unbindCoursesFromDepartment(id);
        doNothing().when(teacherRepository).unbindTeachersFromDepartment(id);
        doNothing().when(departmentRepository).deleteById(id);

        boolean result = departmentService.deleteById(1);

        assertTrue(result);
        verify(departmentRepository, times(1)).findById(id);
        verify(departmentRepository, times(1)).deleteById(id);
        verify(courseRepository, times(1)).unbindCoursesFromDepartment(id);
        verify(teacherRepository, times(1)).unbindTeachersFromDepartment(id);
    }

    @Test
    void deleteById_inputDepartmentId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(departmentRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = departmentService.deleteById(1);

        assertFalse(result);
        verify(departmentRepository, times(1)).findById(id);
        verifyNoInteractions(courseRepository);
        verifyNoInteractions(teacherRepository);
        verify(departmentRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputDepartmentsIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(courseRepository).unbindCoursesFromDepartment(1);
        doNothing().when(teacherRepository).unbindTeachersFromDepartment(1);
        doNothing().when(courseRepository).unbindCoursesFromDepartment(2);
        doNothing().when(teacherRepository).unbindTeachersFromDepartment(2);
        doNothing().when(departmentRepository).deleteAllByIdInBatch(ids);

        boolean result = departmentService.deleteByIds(ids);

        assertTrue(result);
        verify(departmentRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(courseRepository, times(1)).unbindCoursesFromDepartment(1);
        verify(teacherRepository, times(1)).unbindTeachersFromDepartment(1);
        verify(courseRepository, times(1)).unbindCoursesFromDepartment(2);
        verify(teacherRepository, times(1)).unbindTeachersFromDepartment(2);
    }

    @Test
    void addCoursesToDepartment_inputDepartmentIdCourseIds_expectedNothing() {
        final int departmentId = 1;
        final List<Integer> courses = new ArrayList<>();
        courses.add(1);
        courses.add(2);

        doNothing().when(departmentRepository).addCourseToDepartment(departmentId, courses.get(0));
        doNothing().when(departmentRepository).addCourseToDepartment(departmentId, courses.get(1));

        assertDoesNotThrow(() -> departmentService.addCoursesToDepartment(departmentId, courses));

        verify(departmentRepository, times(1)).addCourseToDepartment(departmentId, courses.get(0));
        verify(departmentRepository, times(1)).addCourseToDepartment(departmentId, courses.get(1));
        verifyNoInteractions(teacherRepository);
        verifyNoInteractions(courseRepository);
    }

    @Test
    void deleteCoursesFromDepartment_inputDepartmentIdCourseIds_expectedNothing() {
        final int departmentId = 1;
        final List<Integer> courses = new ArrayList<>();
        courses.add(1);
        courses.add(2);

        doNothing().when(departmentRepository).deleteCourseFromDepartment(departmentId, courses.get(0));
        doNothing().when(departmentRepository).deleteCourseFromDepartment(departmentId, courses.get(1));

        assertDoesNotThrow(() -> departmentService.deleteCoursesFromDepartment(departmentId, courses));

        verify(departmentRepository, times(1)).deleteCourseFromDepartment(departmentId, courses.get(0));
        verify(departmentRepository, times(1)).deleteCourseFromDepartment(departmentId, courses.get(1));
        verifyNoInteractions(teacherRepository);
        verifyNoInteractions(courseRepository);
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(departmentRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, departmentService.lastPageNumber());

        verify(departmentRepository, times(1)).count();
    }

}
