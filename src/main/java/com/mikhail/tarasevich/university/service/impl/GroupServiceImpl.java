package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.GroupRequest;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.GroupMapper;
import com.mikhail.tarasevich.university.service.GroupService;
import com.mikhail.tarasevich.university.validator.GroupValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupServiceImpl extends AbstractPageableService implements GroupService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupDao groupDao;
    private final LessonDao lessonDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final GroupMapper mapper;
    private final GroupValidator validator;

    @Autowired
    public GroupServiceImpl(GroupDao groupDao, LessonDao lessonDao, StudentDao studentDao, TeacherDao teacherDao,
                            GroupMapper mapper, GroupValidator validator) {
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public GroupResponse register(GroupRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(groupDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<GroupRequest> requests) {
        List<GroupRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The group were deleted from the save list. The group: {} .", r);
            }
        });

        groupDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("The groups were saved in the database. Saved groups: {} .", acceptableRequests);
    }

    @Override
    public Optional<GroupResponse> findById(int id) {
        return groupDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<GroupResponse> findAll(String page) {
        final long itemsCount = groupDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return groupDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(GroupRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        groupDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<GroupRequest> requests) {
        List<GroupRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The group was deleted from the update list. The group: {} .", r);
            }
        });

        groupDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Group> optionalCourseEntities = groupDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return groupDao.deleteById(id);
        } else {
            LOG.info("Delete was rejected. There is no group with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = groupDao.deleteByIds(ids);

        if (result) LOG.info("The group have been deleted. Deleted groups: {}", ids);

        return result;
    }

    @Override
    public List<GroupResponse> findGroupsRelateToTeacher(int teacherId) {
        return groupDao.findGroupsRelateToTeacher(teacherId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    private void unbindDependenciesBeforeDelete(int id) {
        lessonDao.unbindLessonsFromGroup(id);
        studentDao.unbindStudentsFromGroup(id);
        teacherDao.unbindTeachersFromGroup(id);
    }

}
