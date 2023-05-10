package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.StudentRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.GroupMapper;
import com.mikhail.tarasevich.university.service.validator.GroupValidator;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @InjectMocks
    GroupServiceImpl groupService;
    @Mock
    GroupRepository groupRepository;
    @Mock
    LessonRepository lessonRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    TeacherRepository teacherRepository;
    @Mock
    GroupMapper mapper;
    @Mock
    GroupValidator validator;

    private static final Group GROUP_ENTITY_1 = Group.builder().withName("name1").build();
    private static final Group GROUP_ENTITY_WITH_ID_1 = Group.builder().withId(1).withName("name1").build();
    private static final GroupRequest GROUP_REQUEST_1 = new GroupRequest();
    private static final GroupResponse GROUP_RESPONSE_WITH_ID_1 = new GroupResponse();

    private static final Group GROUP_ENTITY_2 = Group.builder().withName("name2").build();
    private static final Group GROUP_ENTITY_WITH_ID_2 = Group.builder().withId(2).withName("name2").build();
    private static final GroupRequest GROUP_REQUEST_2 = new GroupRequest();
    private static final GroupResponse GROUP_RESPONSE_WITH_ID_2 = new GroupResponse();

    private static final List<Group> groupEntities = new ArrayList<>();
    private static final List<Group> groupEntitiesWithId = new ArrayList<>();
    private static final List<GroupResponse> groupResponses = new ArrayList<>();

    static {
        GROUP_REQUEST_1.setId(0);
        GROUP_REQUEST_1.setName("name1");

        GROUP_RESPONSE_WITH_ID_1.setId(1);
        GROUP_RESPONSE_WITH_ID_1.setName("name1");

        GROUP_REQUEST_2.setId(0);
        GROUP_REQUEST_2.setName("name2");

        GROUP_RESPONSE_WITH_ID_2.setId(2);
        GROUP_RESPONSE_WITH_ID_2.setName("name2");

        groupEntities.add(GROUP_ENTITY_1);
        groupEntities.add(GROUP_ENTITY_2);

        groupEntitiesWithId.add(GROUP_ENTITY_WITH_ID_1);
        groupEntitiesWithId.add(GROUP_ENTITY_WITH_ID_2);

        groupResponses.add(GROUP_RESPONSE_WITH_ID_1);
        groupResponses.add(GROUP_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputGroupRequest_expectedGroupResponseWithId() {
        when(mapper.toEntity(GROUP_REQUEST_1)).thenReturn(GROUP_ENTITY_1);
        when(groupRepository.save(GROUP_ENTITY_1)).thenReturn(GROUP_ENTITY_WITH_ID_1);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(GROUP_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(GROUP_REQUEST_1);

        GroupResponse groupResponse = groupService.register(GROUP_REQUEST_1);

        assertEquals(GROUP_RESPONSE_WITH_ID_1, groupResponse);
        verify(mapper, times(1)).toEntity(GROUP_REQUEST_1);
        verify(groupRepository, times(1)).save(GROUP_ENTITY_1);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(GROUP_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(GROUP_REQUEST_1);
    }

    @Test
    void registerAllGroups_inputGroupRequestListWithRepeatableGroup_expectedNothing() {
        final List<GroupRequest> listForRegister = new ArrayList<>();
        listForRegister.add(GROUP_REQUEST_1);
        listForRegister.add(GROUP_REQUEST_1);
        listForRegister.add(GROUP_REQUEST_2);

        when(mapper.toEntity(GROUP_REQUEST_1)).thenReturn(GROUP_ENTITY_1);
        when(mapper.toEntity(GROUP_REQUEST_2)).thenReturn(GROUP_ENTITY_2);
        doNothing().doThrow(new IncorrectRequestDataException()).when(validator).validateUniqueNameInDB(GROUP_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(GROUP_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(GROUP_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(GROUP_REQUEST_2);

        groupService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(GROUP_REQUEST_1);
        verify(mapper, times(1)).toEntity(GROUP_REQUEST_2);
        verify(groupRepository, times(1)).saveAll(groupEntities);
        verify(validator, times(2)).validateUniqueNameInDB(GROUP_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(GROUP_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(GROUP_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(GROUP_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundGroup() {
        when(groupRepository.findById(1)).thenReturn(Optional.of(GROUP_ENTITY_WITH_ID_1));
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);

        GroupResponse groupResponse = groupService.findById(1);

        assertEquals(GROUP_RESPONSE_WITH_ID_1, groupResponse);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(groupRepository, times(1)).findById(1);
    }

    @Test
    void findById_inputIncorrectId_expectedException() {
        when(groupRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> groupService.findById(100));

        verify(groupRepository, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllGroups() {
        when(groupRepository.findAll()).thenReturn(groupEntitiesWithId);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_2)).thenReturn(GROUP_RESPONSE_WITH_ID_2);

        List<GroupResponse> foundGroups = groupService.findAll();

        assertEquals(groupResponses, foundGroups);
        verify(groupRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundGroupsFromPageOne() {
        Page<Group> pageOfGroupEntitiesWithId = new PageImpl<>(groupEntitiesWithId);

        when(groupRepository
                .findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id"))))
                .thenReturn(pageOfGroupEntitiesWithId);
        when(groupRepository.count()).thenReturn(2L);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_2)).thenReturn(GROUP_RESPONSE_WITH_ID_2);

        List<GroupResponse> foundGroups = groupService.findAll("1");

        assertEquals(groupResponses, foundGroups);
        verify(groupRepository, times(1)).findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id")));
        verify(groupRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_2);
    }

    @Test
    void findGroupsNotRelateToTeacher_inputTeacherId_expectedFoundGroupsFromDB() {
        when(groupRepository.findGroupsNotRelateToTeacher(1)).thenReturn(groupEntitiesWithId);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_2)).thenReturn(GROUP_RESPONSE_WITH_ID_2);

        List<GroupResponse> foundGroups = groupService.findGroupsNotRelateToTeacher(1);

        assertEquals(groupResponses, foundGroups);
        verify(groupRepository, times(1)).findGroupsNotRelateToTeacher(1);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputGroupRequest_expectedNothing() {
        final Group GROUP_ENTITY_FOR_UPDATE_1 = Group.builder()
                .withId(1)
                .withName("update1")
                .withFaculty(Faculty.builder().withId(1).build())
                .withEducationForm(EducationForm.builder().withId(1).build())
                .withHeadStudent(Student.builder().withId(1).build())
                .build();
        final GroupRequest GROUP_REQUEST_FOR_UPDATE_1 = new GroupRequest();
        GROUP_REQUEST_FOR_UPDATE_1.setId(1);
        GROUP_REQUEST_FOR_UPDATE_1.setName("update1");


        doNothing().when(groupRepository).update(GROUP_ENTITY_FOR_UPDATE_1.getId(), GROUP_ENTITY_FOR_UPDATE_1.getName(),
                1, 1, 1);
        when(mapper.toEntity(GROUP_REQUEST_FOR_UPDATE_1)).thenReturn(GROUP_ENTITY_FOR_UPDATE_1);

        groupService.edit(GROUP_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(GROUP_REQUEST_FOR_UPDATE_1);
        verify(groupRepository, times(1)).update(GROUP_ENTITY_FOR_UPDATE_1.getId(),
                GROUP_ENTITY_FOR_UPDATE_1.getName(), 1, 1, 1);
    }

    @Test
    void editAll_inputGroupRequestListWhereOneGroupHasIncorrectName_expectedNothing() {
        final Group GROUP_ENTITY_FOR_UPDATE_1 = Group.builder().withId(1).withName("update1").build();
        final GroupRequest GROUP_REQUEST_FOR_UPDATE_1 = new GroupRequest();
        GROUP_REQUEST_FOR_UPDATE_1.setId(1);
        GROUP_REQUEST_FOR_UPDATE_1.setName("update1");

        final Group GROUP_ENTITY_FOR_UPDATE_2 = Group.builder().withId(2).withName("update2").build();
        final GroupRequest GROUP_REQUEST_FOR_UPDATE_2 = new GroupRequest();
        GROUP_REQUEST_FOR_UPDATE_2.setId(2);
        GROUP_REQUEST_FOR_UPDATE_2.setName("update2");

        final GroupRequest GROUP_REQUEST_FOR_UPDATE_INCORRECT = new GroupRequest();
        GROUP_REQUEST_FOR_UPDATE_INCORRECT.setId(0);
        GROUP_REQUEST_FOR_UPDATE_INCORRECT.setName("   ");

        final List<GroupRequest> inputList = new ArrayList<>();
        inputList.add(GROUP_REQUEST_FOR_UPDATE_1);
        inputList.add(GROUP_REQUEST_FOR_UPDATE_2);
        inputList.add(GROUP_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Group> listForUpdate = new ArrayList<>();
        listForUpdate.add(GROUP_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(GROUP_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(GROUP_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(GROUP_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator)
                .validateNameNotNullOrEmpty(GROUP_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(GROUP_REQUEST_FOR_UPDATE_1)).thenReturn(GROUP_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(GROUP_REQUEST_FOR_UPDATE_2)).thenReturn(GROUP_ENTITY_FOR_UPDATE_2);

        groupService.editAll(inputList);

        verify(mapper, times(1)).toEntity(GROUP_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(GROUP_REQUEST_FOR_UPDATE_2);
        verify(groupRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1)).validateNameNotNullOrEmpty(GROUP_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(GROUP_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(GROUP_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputGroupId_expectedSuccessDelete() {
        int id = 1;

        when(groupRepository.findById(id)).thenReturn(Optional.of(GROUP_ENTITY_1));
        doNothing().when(lessonRepository).unbindLessonsFromGroup(id);
        doNothing().when(studentRepository).unbindStudentsFromGroup(id);
        doNothing().when(teacherRepository).unbindTeachersFromGroup(id);
        doNothing().when(groupRepository).deleteById(id);

        boolean result = groupService.deleteById(1);

        assertTrue(result);
        verify(groupRepository, times(1)).findById(id);
        verify(groupRepository, times(1)).deleteById(id);
        verify(lessonRepository, times(1)).unbindLessonsFromGroup(id);
        verify(studentRepository, times(1)).unbindStudentsFromGroup(id);
        verify(teacherRepository, times(1)).unbindTeachersFromGroup(id);
    }

    @Test
    void deleteById_inputGroupId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(groupRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = groupService.deleteById(1);

        assertFalse(result);
        verify(groupRepository, times(1)).findById(id);
        verifyNoInteractions(lessonRepository);
        verifyNoInteractions(studentRepository);
        verifyNoInteractions(teacherRepository);
        verify(groupRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputGroupsIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(lessonRepository).unbindLessonsFromGroup(1);
        doNothing().when(studentRepository).unbindStudentsFromGroup(1);
        doNothing().when(teacherRepository).unbindTeachersFromGroup(1);
        doNothing().when(lessonRepository).unbindLessonsFromGroup(2);
        doNothing().when(studentRepository).unbindStudentsFromGroup(2);
        doNothing().when(teacherRepository).unbindTeachersFromGroup(2);
        doNothing().when(groupRepository).deleteAllByIdInBatch(ids);

        boolean result = groupService.deleteByIds(ids);

        assertTrue(result);
        verify(groupRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(lessonRepository, times(1)).unbindLessonsFromGroup(1);
        verify(studentRepository, times(1)).unbindStudentsFromGroup(1);
        verify(teacherRepository, times(1)).unbindTeachersFromGroup(1);
        verify(lessonRepository, times(1)).unbindLessonsFromGroup(2);
        verify(studentRepository, times(1)).unbindStudentsFromGroup(2);
        verify(teacherRepository, times(1)).unbindTeachersFromGroup(2);
    }

    @Test
    void findGroupsRelateToTeacher_inputTeacherId_expectedGroupList() {
        when(groupRepository.findGroupByTeachersId(1)).thenReturn(groupEntitiesWithId);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_2)).thenReturn(GROUP_RESPONSE_WITH_ID_2);

        List<GroupResponse> foundGroups = groupService.findGroupsRelateToTeacher(1);

        assertEquals(groupResponses, foundGroups);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_2);
        verify(groupRepository, times(1)).findGroupByTeachersId(1);
    }

    @Test
    void findGroupsRelateToFaculty_inputFacultyId_expectedGroupList() {
        final Group GROUP_ENTITY_WITH_ID_1 = Group.builder()
                .withId(1)
                .withName("name1")
                .withFaculty(Faculty.builder().withId(1).build())
                .build();
        final Group GROUP_ENTITY_WITH_ID_2 = Group.builder()
                .withId(2)
                .withName("name2")
                .withFaculty(Faculty.builder().withId(2).build())
                .build();

        final List<Group> groups = new ArrayList<>();
        groups.add(GROUP_ENTITY_WITH_ID_1);
        groups.add(GROUP_ENTITY_WITH_ID_2);

        final List<GroupResponse> expected = new ArrayList<>();
        expected.add(GROUP_RESPONSE_WITH_ID_1);

        when(groupRepository.findAll()).thenReturn(groups);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);

        List<GroupResponse> foundGroups = groupService.findGroupsRelateToFaculty(1);

        assertEquals(expected, foundGroups);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void findGroupsRelateToEducationForm_inputEducationFormId_expectedGroupList() {
        final Group GROUP_ENTITY_WITH_ID_1 = Group.builder()
                .withId(1)
                .withName("name1")
                .withEducationForm(EducationForm.builder().withId(1).build())
                .build();
        final Group GROUP_ENTITY_WITH_ID_2 = Group.builder()
                .withId(2)
                .withName("name2")
                .withEducationForm(EducationForm.builder().withId(2).build())
                .build();

        final List<Group> groups = new ArrayList<>();
        groups.add(GROUP_ENTITY_WITH_ID_1);
        groups.add(GROUP_ENTITY_WITH_ID_2);

        final List<GroupResponse> expected = new ArrayList<>();
        expected.add(GROUP_RESPONSE_WITH_ID_1);

        when(groupRepository.findAll()).thenReturn(groups);
        when(mapper.toResponse(GROUP_ENTITY_WITH_ID_1)).thenReturn(GROUP_RESPONSE_WITH_ID_1);

        List<GroupResponse> foundGroups = groupService.findGroupsRelateToEducationForm(1);

        assertEquals(expected, foundGroups);
        verify(mapper, times(1)).toResponse(GROUP_ENTITY_WITH_ID_1);
        verify(groupRepository, times(1)).findAll();
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(groupRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, groupService.lastPageNumber());

        verify(groupRepository, times(1)).count();
    }

}
