package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.DepartmentRequest;
import com.mikhail.tarasevich.university.dto.DepartmentResponse;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.repository.CourseRepository;
import com.mikhail.tarasevich.university.repository.DepartmentRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.DepartmentMapper;
import com.mikhail.tarasevich.university.service.DepartmentService;
import com.mikhail.tarasevich.university.service.validator.DepartmentValidator;
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
public class DepartmentServiceImpl extends AbstractPageableService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final DepartmentMapper mapper;
    private final DepartmentValidator validator;

    @Override
    public DepartmentResponse register(DepartmentRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(departmentRepository.save(mapper.toEntity(r)));
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

        departmentRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("Departments were saved in the database. Saved requests: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse findById(int id) {
        Optional<DepartmentResponse> foundDepartment = departmentRepository.findById(id).map(mapper::toResponse);

        if (foundDepartment.isPresent()) {
            return foundDepartment.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The department with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll(String page) {
        final long itemsCount = departmentRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return departmentRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> findAll() {
        return departmentRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(DepartmentRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        Department d = mapper.toEntity(r);
        departmentRepository.update(d.getId(), d.getName(), d.getDescription());
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

        departmentRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Department> optionalCourseEntities = departmentRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            departmentRepository.deleteById(id);

            return true;
        } else {
            log.info("Delete was rejected. There is no department with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        departmentRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    public void addCoursesToDepartment(int departmentId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> departmentRepository.addCourseToDepartment(departmentId, courseId));
        log.info("Courses with ids = {} have been added to department with id = {} ", courseIds, departmentId);
    }

    @Override
    public void deleteCoursesFromDepartment(int departmentId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> departmentRepository.deleteCourseFromDepartment(departmentId, courseId));
        log.info("Courses with ids = {} have been deleted from department with id = {} ", courseIds, departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) departmentRepository.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        courseRepository.unbindCoursesFromDepartment(id);
        teacherRepository.unbindTeachersFromDepartment(id);
    }

}
