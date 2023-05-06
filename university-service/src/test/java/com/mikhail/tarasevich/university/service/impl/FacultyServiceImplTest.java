package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.FacultyRequest;
import com.mikhail.tarasevich.university.dto.FacultyResponse;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.mapper.FacultyMapper;
import com.mikhail.tarasevich.university.repository.FacultyRepository;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.service.validator.FacultyValidator;
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
class FacultyServiceImplTest {

    @InjectMocks
    FacultyServiceImpl facultyService;
    @Mock
    FacultyRepository facultyRepository;
    @Mock
    GroupRepository groupRepository;
    @Mock
    FacultyMapper mapper;
    @Mock
    FacultyValidator validator;

    private static final Faculty FACULTY_ENTITY_1 = Faculty.builder()
            .withName("name1")
            .withDescription("description1")
            .build();
    private static final Faculty FACULTY_ENTITY_WITH_ID_1 = Faculty.builder()
            .withId(1)
            .withName("name1")
            .withDescription("description1")
            .build();
    private static final FacultyRequest FACULTY_REQUEST_1 = new FacultyRequest();
    private static final FacultyResponse FACULTY_RESPONSE_WITH_ID_1 = new FacultyResponse();

    private static final Faculty FACULTY_ENTITY_2 = Faculty.builder()
            .withName("name2")
            .withDescription("description2")
            .build();
    private static final Faculty FACULTY_ENTITY_WITH_ID_2 = Faculty.builder()
            .withId(2)
            .withName("name2")
            .withDescription("description2")
            .build();
    private static final FacultyRequest FACULTY_REQUEST_2 = new FacultyRequest();
    private static final FacultyResponse FACULTY_RESPONSE_WITH_ID_2 = new FacultyResponse();

    private static final List<Faculty> facultyEntities = new ArrayList<>();
    private static final List<Faculty> facultyEntitiesWithId = new ArrayList<>();
    private static final List<FacultyResponse> facultyResponses = new ArrayList<>();

    static {
        FACULTY_REQUEST_1.setId(0);
        FACULTY_REQUEST_1.setName("name1");
        FACULTY_REQUEST_1.setDescription("description1");

        FACULTY_RESPONSE_WITH_ID_1.setId(1);
        FACULTY_RESPONSE_WITH_ID_1.setName("name1");
        FACULTY_RESPONSE_WITH_ID_1.setDescription("description1");

        FACULTY_REQUEST_2.setId(0);
        FACULTY_REQUEST_2.setName("name2");
        FACULTY_REQUEST_2.setDescription("description2");

        FACULTY_RESPONSE_WITH_ID_2.setId(2);
        FACULTY_RESPONSE_WITH_ID_2.setName("name2");
        FACULTY_RESPONSE_WITH_ID_2.setDescription("description2");

        facultyEntities.add(FACULTY_ENTITY_1);
        facultyEntities.add(FACULTY_ENTITY_2);

        facultyEntitiesWithId.add(FACULTY_ENTITY_WITH_ID_1);
        facultyEntitiesWithId.add(FACULTY_ENTITY_WITH_ID_2);

        facultyResponses.add(FACULTY_RESPONSE_WITH_ID_1);
        facultyResponses.add(FACULTY_RESPONSE_WITH_ID_2);
    }

    @Test
    void register_inputFacultyRequest_expectedFacultyResponseWithId() {
        when(mapper.toEntity(FACULTY_REQUEST_1)).thenReturn(FACULTY_ENTITY_1);
        when(facultyRepository.save(FACULTY_ENTITY_1)).thenReturn(FACULTY_ENTITY_WITH_ID_1);
        when(mapper.toResponse(FACULTY_ENTITY_WITH_ID_1)).thenReturn(FACULTY_RESPONSE_WITH_ID_1);
        doNothing().when(validator).validateUniqueNameInDB(FACULTY_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(FACULTY_REQUEST_1);

        FacultyResponse facultyResponse = facultyService.register(FACULTY_REQUEST_1);

        assertEquals(FACULTY_RESPONSE_WITH_ID_1, facultyResponse);
        verify(mapper, times(1)).toEntity(FACULTY_REQUEST_1);
        verify(facultyRepository, times(1)).save(FACULTY_ENTITY_1);
        verify(mapper, times(1)).toResponse(FACULTY_ENTITY_WITH_ID_1);
        verify(validator, times(1)).validateUniqueNameInDB(FACULTY_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(FACULTY_REQUEST_1);
    }

    @Test
    void registerAllFaculties_inputFacultyRequestListWithRepeatableFaculty_expectedNothing() {
        final List<FacultyRequest> listForRegister = new ArrayList<>();
        listForRegister.add(FACULTY_REQUEST_1);
        listForRegister.add(FACULTY_REQUEST_1);
        listForRegister.add(FACULTY_REQUEST_2);

        when(mapper.toEntity(FACULTY_REQUEST_1)).thenReturn(FACULTY_ENTITY_1);
        when(mapper.toEntity(FACULTY_REQUEST_2)).thenReturn(FACULTY_ENTITY_2);
        doNothing().doThrow(new IncorrectRequestDataException())
                .when(validator).validateUniqueNameInDB(FACULTY_REQUEST_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(FACULTY_REQUEST_1);
        doNothing().when(validator).validateUniqueNameInDB(FACULTY_REQUEST_2);
        doNothing().when(validator).validateNameNotNullOrEmpty(FACULTY_REQUEST_2);

        facultyService.registerAll(listForRegister);

        verify(mapper, times(1)).toEntity(FACULTY_REQUEST_1);
        verify(mapper, times(1)).toEntity(FACULTY_REQUEST_2);
        verify(facultyRepository, times(1)).saveAll(facultyEntities);
        verify(validator, times(2)).validateUniqueNameInDB(FACULTY_REQUEST_1);
        verify(validator, times(1)).validateNameNotNullOrEmpty(FACULTY_REQUEST_1);
        verify(validator, times(1)).validateUniqueNameInDB(FACULTY_REQUEST_2);
        verify(validator, times(1)).validateNameNotNullOrEmpty(FACULTY_REQUEST_2);
    }

    @Test
    void findById_inputIntId_expectedFoundFaculty() {
        when(facultyRepository.findById(1)).thenReturn(Optional.of(FACULTY_ENTITY_WITH_ID_1));
        when(mapper.toResponse(FACULTY_ENTITY_WITH_ID_1)).thenReturn(FACULTY_RESPONSE_WITH_ID_1);

        FacultyResponse facultyResponse = facultyService.findById(1);

        assertEquals(FACULTY_RESPONSE_WITH_ID_1, facultyResponse);
        verify(mapper, times(1)).toResponse(FACULTY_ENTITY_WITH_ID_1);
        verify(facultyRepository, times(1)).findById(1);
    }

    @Test
    void findById_inputIncorrectId_expectedException() {
        when(facultyRepository.findById(100)).thenReturn(Optional.empty());

        assertThrows(ObjectWithSpecifiedIdNotFoundException.class, () -> facultyService.findById(100));

        verify(facultyRepository, times(1)).findById(100);
        verifyNoInteractions(mapper);
    }

    @Test
    void findAll_inputNothing_expectedFoundAllFaculties() {
        when(facultyRepository.findAll()).thenReturn(facultyEntitiesWithId);
        when(mapper.toResponse(FACULTY_ENTITY_WITH_ID_1)).thenReturn(FACULTY_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(FACULTY_ENTITY_WITH_ID_2)).thenReturn(FACULTY_RESPONSE_WITH_ID_2);

        List<FacultyResponse> foundFaculties = facultyService.findAll();

        assertEquals(facultyResponses, foundFaculties);
        verify(facultyRepository, times(1)).findAll();
        verify(mapper, times(1)).toResponse(FACULTY_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(FACULTY_ENTITY_WITH_ID_2);
    }

    @Test
    void findAll_inputPageOne_expectedFoundFacultiesFromPageOne() {
        Page<Faculty> pageOfFacultyEntitiesWithId = new PageImpl<>(facultyEntitiesWithId);

        when(facultyRepository
                .findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id"))))
                .thenReturn(pageOfFacultyEntitiesWithId);
        when(facultyRepository.count()).thenReturn(2L);
        when(mapper.toResponse(FACULTY_ENTITY_WITH_ID_1)).thenReturn(FACULTY_RESPONSE_WITH_ID_1);
        when(mapper.toResponse(FACULTY_ENTITY_WITH_ID_2)).thenReturn(FACULTY_RESPONSE_WITH_ID_2);

        List<FacultyResponse> foundFaculties = facultyService.findAll("1");

        assertEquals(facultyResponses, foundFaculties);
        verify(facultyRepository, times(1)).findAll(PageRequest.of(0, AbstractPageableService.ITEMS_PER_PAGE, Sort.by("id")));
        verify(facultyRepository, times(1)).count();
        verify(mapper, times(1)).toResponse(FACULTY_ENTITY_WITH_ID_1);
        verify(mapper, times(1)).toResponse(FACULTY_ENTITY_WITH_ID_2);
    }

    @Test
    void edit_inputFacultyRequest_expectedNothing() {
        final Faculty FACULTY_ENTITY_FOR_UPDATE_1 = Faculty.builder()
                .withId(1)
                .withName("update1")
                .withDescription("update1")
                .build();
        final FacultyRequest FACULTY_REQUEST_FOR_UPDATE_1 = new FacultyRequest();
        FACULTY_REQUEST_FOR_UPDATE_1.setId(1);
        FACULTY_REQUEST_FOR_UPDATE_1.setName("update1");
        FACULTY_REQUEST_FOR_UPDATE_1.setDescription("update1");

        doNothing().when(facultyRepository).update(FACULTY_REQUEST_FOR_UPDATE_1.getId(),
                FACULTY_REQUEST_FOR_UPDATE_1.getName(), FACULTY_REQUEST_FOR_UPDATE_1.getDescription());
        when(mapper.toEntity(FACULTY_REQUEST_FOR_UPDATE_1)).thenReturn(FACULTY_ENTITY_FOR_UPDATE_1);

        facultyService.edit(FACULTY_REQUEST_FOR_UPDATE_1);

        verify(mapper, times(1)).toEntity(FACULTY_REQUEST_FOR_UPDATE_1);
        verify(facultyRepository, times(1)).update(FACULTY_REQUEST_FOR_UPDATE_1.getId(),
                FACULTY_REQUEST_FOR_UPDATE_1.getName(), FACULTY_REQUEST_FOR_UPDATE_1.getDescription());
    }

    @Test
    void editAll_inputFacultyRequestListWhereOneFacultyHasIncorrectName_expectedNothing() {
        final Faculty FACULTY_ENTITY_FOR_UPDATE_1 = Faculty.builder()
                .withId(1)
                .withName("update1")
                .withDescription("update1")
                .build();
        final FacultyRequest FACULTY_REQUEST_FOR_UPDATE_1 = new FacultyRequest();
        FACULTY_REQUEST_FOR_UPDATE_1.setId(1);
        FACULTY_REQUEST_FOR_UPDATE_1.setName("update1");
        FACULTY_REQUEST_FOR_UPDATE_1.setDescription("update1");

        final Faculty FACULTY_ENTITY_FOR_UPDATE_2 = Faculty.builder()
                .withId(2)
                .withName("update2")
                .withDescription("update2")
                .build();
        final FacultyRequest FACULTY_REQUEST_FOR_UPDATE_2 = new FacultyRequest();
        FACULTY_REQUEST_FOR_UPDATE_2.setId(2);
        FACULTY_REQUEST_FOR_UPDATE_2.setName("update2");
        FACULTY_REQUEST_FOR_UPDATE_2.setDescription("update2");

        final FacultyRequest FACULTY_REQUEST_FOR_UPDATE_INCORRECT = new FacultyRequest();
        FACULTY_REQUEST_FOR_UPDATE_INCORRECT.setId(3);
        FACULTY_REQUEST_FOR_UPDATE_INCORRECT.setName("  ");
        FACULTY_REQUEST_FOR_UPDATE_INCORRECT.setDescription("update3");

        final List<FacultyRequest> inputList = new ArrayList<>();
        inputList.add(FACULTY_REQUEST_FOR_UPDATE_1);
        inputList.add(FACULTY_REQUEST_FOR_UPDATE_2);
        inputList.add(FACULTY_REQUEST_FOR_UPDATE_INCORRECT);

        final List<Faculty> listForUpdate = new ArrayList<>();
        listForUpdate.add(FACULTY_ENTITY_FOR_UPDATE_1);
        listForUpdate.add(FACULTY_ENTITY_FOR_UPDATE_2);

        doNothing().when(validator).validateNameNotNullOrEmpty(FACULTY_REQUEST_FOR_UPDATE_1);
        doNothing().when(validator).validateNameNotNullOrEmpty(FACULTY_REQUEST_FOR_UPDATE_2);
        doThrow(new IncorrectRequestDataException()).when(validator)
                .validateNameNotNullOrEmpty(FACULTY_REQUEST_FOR_UPDATE_INCORRECT);

        when(mapper.toEntity(FACULTY_REQUEST_FOR_UPDATE_1)).thenReturn(FACULTY_ENTITY_FOR_UPDATE_1);
        when(mapper.toEntity(FACULTY_REQUEST_FOR_UPDATE_2)).thenReturn(FACULTY_ENTITY_FOR_UPDATE_2);

        facultyService.editAll(inputList);

        verify(mapper, times(1)).toEntity(FACULTY_REQUEST_FOR_UPDATE_1);
        verify(mapper, times(1)).toEntity(FACULTY_REQUEST_FOR_UPDATE_2);
        verify(facultyRepository, times(1)).saveAll(listForUpdate);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(FACULTY_REQUEST_FOR_UPDATE_1);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(FACULTY_REQUEST_FOR_UPDATE_2);
        verify(validator, times(1))
                .validateNameNotNullOrEmpty(FACULTY_REQUEST_FOR_UPDATE_INCORRECT);
    }

    @Test
    void deleteById_inputFacultyId_expectedSuccessDelete() {
        int id = 1;

        when(facultyRepository.findById(id)).thenReturn(Optional.of(FACULTY_ENTITY_WITH_ID_1));
        doNothing().when(groupRepository).unbindGroupsFromFaculty(id);
        doNothing().when(facultyRepository).deleteById(id);

        boolean result = facultyService.deleteById(1);

        assertTrue(result);
        verify(facultyRepository, times(1)).findById(id);
        verify(facultyRepository, times(1)).deleteById(id);
        verify(groupRepository, times(1)).unbindGroupsFromFaculty(id);
    }

    @Test
    void deleteById_inputFacultyId_expectedFalseUnsuccessfulDelete() {
        int id = 1;

        when(facultyRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = facultyService.deleteById(1);

        assertFalse(result);
        verify(facultyRepository, times(1)).findById(id);
        verifyNoInteractions(groupRepository);
        verify(facultyRepository, times(0)).deleteById(id);
    }

    @Test
    void deleteByIds_inputFacultiesIds_expectedSuccessDeletes() {
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        ids.add(2);

        doNothing().when(groupRepository).unbindGroupsFromFaculty(1);
        doNothing().when(groupRepository).unbindGroupsFromFaculty(2);
        doNothing().when(facultyRepository).deleteAllByIdInBatch(ids);

        boolean result = facultyService.deleteByIds(ids);

        assertTrue(result);
        verify(facultyRepository, times(1)).deleteAllByIdInBatch(ids);
        verify(groupRepository, times(1)).unbindGroupsFromFaculty(1);
        verify(groupRepository, times(1)).unbindGroupsFromFaculty(2);
    }

    @Test
    void lastPageNumber_inputNothing_expectedLastPageNumber() {
        when(facultyRepository.count()).thenReturn(5L);

        int expected = (int) Math.ceil(5.0 / AbstractPageableService.ITEMS_PER_PAGE);

        assertEquals(expected, facultyService.lastPageNumber());

        verify(facultyRepository, times(1)).count();
    }

}
