package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.DepartmentMapper;
import com.mikhail.tarasevich.university.validator.DepartmentValidator;
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
class DepartmentServiceImplTest {

    @InjectMocks
    DepartmentServiceImpl departmentService;
    @Mock
    DepartmentDao departmentDao;
    @Mock
    CourseDao courseDao;
    @Mock
    TeacherDao teacherDao;
    @Mock
    DepartmentMapper departmentMapper;
    @Mock
    DepartmentValidator departmentValidator;

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
        when(departmentMapper.toEntity(DEPARTMENT_REQUEST_1)).thenReturn(DEPARTMENT_ENTITY_1);
        when(departmentDao.save(DEPARTMENT_ENTITY_1)).thenReturn(DEPARTMENT_ENTITY_WITH_ID_1);
        when(departmentMapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);
        doNothing().when(departmentValidator).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        doNothing().when(departmentValidator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_1);

        DepartmentResponse departmentResponse = departmentService.register(DEPARTMENT_REQUEST_1);

        assertEquals(DEPARTMENT_RESPONSE_WITH_ID_1, departmentResponse);
        verify(departmentMapper, times(1)).toEntity(DEPARTMENT_REQUEST_1);
        verify(departmentDao, times(1)).save(DEPARTMENT_ENTITY_1);
        verify(departmentMapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(departmentValidator, times(1)).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        verify(departmentValidator, times(1)).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
    }

    @Test
    void registerAllDepartments_inputDepartmentRequestListWithRepeatableDepartment_expectedNothing() {
        final List<DepartmentRequest> listForRegister = new ArrayList<>();
        listForRegister.add(DEPARTMENT_REQUEST_1);
        listForRegister.add(DEPARTMENT_REQUEST_2);
        listForRegister.add(DEPARTMENT_REQUEST_1);

        when(departmentMapper.toEntity(DEPARTMENT_REQUEST_1)).thenReturn(DEPARTMENT_ENTITY_1);
        when(departmentMapper.toEntity(DEPARTMENT_REQUEST_2)).thenReturn(DEPARTMENT_ENTITY_2);
        doNothing().when(departmentDao).saveAll(departmentEntities);
        doNothing().doThrow(new IncorrectRequestData()).when(departmentValidator)
                .validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        doNothing().when(departmentValidator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_1);
        doNothing().when(departmentValidator).validateUniqueNameInDB(DEPARTMENT_REQUEST_2);
        doNothing().when(departmentValidator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_2);

        departmentService.registerAll(listForRegister);

        verify(departmentMapper, times(1)).toEntity(DEPARTMENT_REQUEST_1);
        verify(departmentMapper, times(1)).toEntity(DEPARTMENT_REQUEST_2);
        verify(departmentDao, times(1)).saveAll(departmentEntities);
        verify(departmentValidator, times(2)).validateUniqueNameInDB(DEPARTMENT_REQUEST_1);
        verify(departmentValidator, times(1)).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_1);
        verify(departmentValidator, times(1)).validateUniqueNameInDB(DEPARTMENT_REQUEST_2);
        verify(departmentValidator, times(1)).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundCourse() {
        when(departmentDao.findById(1)).thenReturn(Optional.of(DEPARTMENT_ENTITY_WITH_ID_1));
        when(departmentMapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);

        Optional<DepartmentResponse> departmentResponse = departmentService.findById(1);

        assertEquals(Optional.of(DEPARTMENT_RESPONSE_WITH_ID_1), departmentResponse);
        verify(departmentMapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(departmentDao, times(1)).findById(1);
    }

    @Test
    void findAll_inputPageOne_expectedFoundDepartmentsFromPageOne() {
        when(departmentDao.findAll(1, AbstractPageableService.ITEMS_PER_PAGE))
                .thenReturn(departmentEntitiesWithId);
        when(departmentDao.count()).thenReturn(2L);
        when(departmentMapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_1)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_1);
        when(departmentMapper.toResponse(DEPARTMENT_ENTITY_WITH_ID_2)).thenReturn(DEPARTMENT_RESPONSE_WITH_ID_2);

        List<DepartmentResponse> foundDepartments = departmentService.findAll("1");

        assertEquals(departmentResponses, foundDepartments);
        verify(departmentDao, times(1)).findAll(1, AbstractPageableService.ITEMS_PER_PAGE);
        verify(departmentDao, times(1)).count();
        verify(departmentMapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_1);
        verify(departmentMapper, times(1)).toResponse(DEPARTMENT_ENTITY_WITH_ID_2);
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

        doNothing().when(departmentDao).update(DEPARTMENT_ENTITY_FOR_UPDATE_1);
        when(departmentMapper.toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1)).thenReturn(DEPARTMENT_ENTITY_FOR_UPDATE_1);

        departmentService.edit(DEPARTMENT_REQUEST_FOR_UPDATE_1);

        verify(departmentMapper, times(1)).toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        verify(departmentDao, times(1)).update(DEPARTMENT_ENTITY_FOR_UPDATE_1);
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

        doNothing().when(departmentValidator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        doNothing().when(departmentValidator).validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestData()).when(departmentValidator)
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT);

        when(departmentMapper.toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1)).thenReturn(DEPARTMENT_ENTITY_FOR_UPDATE_1);
        when(departmentMapper.toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_2)).thenReturn(DEPARTMENT_ENTITY_FOR_UPDATE_2);

        doNothing().when(departmentDao).updateAll(listForUpdate);

        departmentService.editAll(inputList);

        verify(departmentMapper, times(1)).toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        verify(departmentMapper, times(1)).toEntity(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        verify(departmentDao, times(1)).updateAll(listForUpdate);
        verify(departmentValidator, times(1))
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_1);
        verify(departmentValidator, times(1))
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_2);
        verify(departmentValidator, times(1))
                .validateNameNotNullOrEmpty(DEPARTMENT_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputDepartmentId_expectedSuccessDelete() {
        int id = 1;

        when(departmentDao.findById(id)).thenReturn(Optional.of(DEPARTMENT_ENTITY_1));
        doNothing().when(courseDao).unbindCoursesFromDepartment(id);
        doNothing().when(teacherDao).unbindTeachersFromDepartment(id);
        when(departmentDao.deleteById(id)).thenReturn(true);

        boolean result = departmentService.deleteById(1);

        assertTrue(result);
        verify(departmentDao, times(1)).findById(id);
        verify(departmentDao, times(1)).deleteById(id);
        verify(courseDao, times(1)).unbindCoursesFromDepartment(id);
        verify(teacherDao, times(1)).unbindTeachersFromDepartment(id);
    }

    @Test
    void deleteById_inputDepartmentId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(departmentDao.findById(id)).thenReturn(Optional.empty());

        boolean result = departmentService.deleteById(1);

        assertFalse(result);
        verify(departmentDao, times(1)).findById(id);
        verifyNoInteractions(courseDao);
        verifyNoInteractions(teacherDao);
        verify(departmentDao, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputDepartmentsIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(courseDao).unbindCoursesFromDepartment(1);
        doNothing().when(teacherDao).unbindTeachersFromDepartment(1);
        doNothing().when(courseDao).unbindCoursesFromDepartment(2);
        doNothing().when(teacherDao).unbindTeachersFromDepartment(2);
        when(departmentDao.deleteByIds(ids)).thenReturn(true);

        boolean result = departmentService.deleteByIds(ids);

        assertTrue(result);
        verify(departmentDao, times(1)).deleteByIds(ids);
        verify(courseDao, times(1)).unbindCoursesFromDepartment(1);
        verify(teacherDao, times(1)).unbindTeachersFromDepartment(1);
        verify(courseDao, times(1)).unbindCoursesFromDepartment(2);
        verify(teacherDao, times(1)).unbindTeachersFromDepartment(2);
    }

}
