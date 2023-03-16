package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.DepartmentMapper;
import com.mikhail.tarasevich.university.service.DepartmentService;
import com.mikhail.tarasevich.university.validator.DepartmentValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
public class DepartmentServiceImpl extends AbstractPageableService implements DepartmentService {

    private final DepartmentDao departmentDao;
    private final CourseDao courseDao;
    private final TeacherDao teacherDao;
    private final DepartmentMapper mapper;
    private final DepartmentValidator validator;

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
            } catch (IncorrectRequestDataException e) {
                log.info("The departments were deleted from the save list. Request: {} .", r);
            }
        });

        departmentDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("Departments were saved in the database. Saved requests: {} .", acceptableRequests);
    }

    @Override
    public DepartmentResponse findById(int id) {
        Optional<DepartmentResponse> foundDepartment = departmentDao.findById(id).map(mapper::toResponse);

        if (foundDepartment.isPresent()) {
            return foundDepartment.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The department with specified id doesn't exist in the database.");
        }
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
    public List<DepartmentResponse> findAll() {
        return departmentDao.findAll().stream()
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
            } catch (IncorrectRequestDataException e) {
                log.info("The department was deleted from the update list. The department: {} .", r);
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
            log.info("Delete was rejected. There is no department with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = departmentDao.deleteByIds(ids);

        if (result) log.info("The department have been deleted. Deleted departments: {}", ids);

        return result;
    }

    @Override
    public void addCoursesToDepartment(int departmentId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> departmentDao.addCourseToDepartment(departmentId, courseId));
        log.info("Courses with ids = {} have been added to department with id = {} ", courseIds, departmentId);
    }

    @Override
    public void deleteCoursesFromDepartment(int departmentId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> departmentDao.deleteCourseFromDepartment(departmentId, courseId));
        log.info("Courses with ids = {} have been deleted from department with id = {} ", courseIds, departmentId);
    }

    @Override
    public int lastPageNumber() {
        return (int) Math.ceil((double) departmentDao.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        courseDao.unbindCoursesFromDepartment(id);
        teacherDao.unbindTeachersFromDepartment(id);
    }

}
