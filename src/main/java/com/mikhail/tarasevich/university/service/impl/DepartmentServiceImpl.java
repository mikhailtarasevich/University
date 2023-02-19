package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.DepartmentMapper;
import com.mikhail.tarasevich.university.service.DepartmentService;
import com.mikhail.tarasevich.university.validator.DepartmentValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentServiceImpl extends AbstractPageableService implements DepartmentService {

    private static final Logger LOG = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    private final DepartmentDao departmentDao;
    private final CourseDao courseDao;
    private final TeacherDao teacherDao;
    private final DepartmentMapper mapper;
    private final DepartmentValidator validator;


    public DepartmentServiceImpl(DepartmentDao departmentDao, CourseDao courseDao, TeacherDao teacherDao,
                                 DepartmentMapper mapper, DepartmentValidator validator) {
        this.departmentDao = departmentDao;
        this.courseDao = courseDao;
        this.teacherDao = teacherDao;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public DepartmentResponse register(DepartmentRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(departmentDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<DepartmentRequest> requests) {
        List<DepartmentRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The departments were deleted from the save list. Request: {} .", r);
            }
        });

        departmentDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        LOG.info("Departments were saved in the database. Saved requests: {} .", acceptableRequests);
    }

    @Override
    public Optional<DepartmentResponse> findById(int id) {
        return departmentDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<DepartmentResponse> findAll(String page) {
        final long itemsCount = departmentDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return departmentDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(DepartmentRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        departmentDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<DepartmentRequest> requests) {
        List<DepartmentRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                LOG.info("The department was deleted from the update list. The department: {} .", r);
            }
        });

        departmentDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Department> optionalCourseEntities = departmentDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return departmentDao.deleteById(id);
        } else {
            LOG.info("Delete was rejected. There is no department with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = departmentDao.deleteByIds(ids);

        if (result) LOG.info("The department have been deleted. Deleted departments: {}", ids);

        return result;
    }

    private void unbindDependenciesBeforeDelete(int id) {
        courseDao.unbindCoursesFromDepartment(id);
        teacherDao.unbindTeachersFromDepartment(id);
    }

}