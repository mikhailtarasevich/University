package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.StudentRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.GroupMapper;
import com.mikhail.tarasevich.university.service.GroupService;
import com.mikhail.tarasevich.university.service.validator.GroupValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class GroupServiceImpl extends AbstractPageableService implements GroupService {

    private final GroupRepository groupRepository;
    private final LessonRepository lessonRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final GroupMapper mapper;
    private final GroupValidator validator;

    @Override
    public GroupResponse register(GroupRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(groupRepository.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<GroupRequest> requests) {
        List<GroupRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The group were deleted from the save list. The group: {} .", r);
            }
        });

        groupRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("The groups were saved in the database. Saved groups: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupResponse findById(int id) {
        Optional<GroupResponse> foundGroup = groupRepository.findById(id).map(mapper::toResponse);

        if (foundGroup.isPresent()) {
            return foundGroup.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The group with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> findAll(String page) {
        final long itemsCount = groupRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return groupRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> findAll() {
        return groupRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(GroupRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        Group g = mapper.toEntity(r);
        groupRepository.update(g.getId(), g.getName(), g.getFaculty().getId(), g.getHeadStudent().getId(),
                g.getEducationForm().getId());
    }

    @Override
    public void editAll(List<GroupRequest> requests) {
        List<GroupRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The group was deleted from the update list. The group: {} .", r);
            }
        });

        groupRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Group> optionalCourseEntities = groupRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            groupRepository.deleteById(id);

            return true;
        } else {
            log.info("Delete was rejected. There is no group with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        groupRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> findGroupsRelateToTeacher(int teacherId) {
        return groupRepository.findGroupByTeachersId(teacherId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> findGroupsNotRelateToTeacher(int teacherId) {
        return groupRepository.findGroupsNotRelateToTeacher(teacherId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> findGroupsRelateToFaculty(int facultyId) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getFaculty() != null && g.getFaculty().getId() == facultyId)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupResponse> findGroupsRelateToEducationForm(int educationFormId) {
        return groupRepository.findAll().stream()
                .filter(g -> g.getEducationForm() != null && g.getEducationForm().getId() == educationFormId)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) groupRepository.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        lessonRepository.unbindLessonsFromGroup(id);
        studentRepository.unbindStudentsFromGroup(id);
        teacherRepository.unbindTeachersFromGroup(id);
    }

}
