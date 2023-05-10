package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.repository.CourseRepository;
import com.mikhail.tarasevich.university.repository.DepartmentRepository;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.exception.IncorrectRequestDataException;
import com.mikhail.tarasevich.university.service.exception.ObjectWithSpecifiedIdNotFoundException;
import com.mikhail.tarasevich.university.mapper.CourseMapper;
import com.mikhail.tarasevich.university.service.CourseService;
import com.mikhail.tarasevich.university.service.validator.CourseValidator;
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
public class CourseServiceImpl extends AbstractPageableService implements CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper mapper;
    private final CourseValidator validator;


    @Override
    public CourseResponse register(CourseRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(courseRepository.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<CourseRequest> requests) {
        List<CourseRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The request were deleted from the save list. Request: {} .", r);
            }
        });

        courseRepository.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("Courses were saved in the database. Saved courses: {} .", acceptableRequests);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse findById(int id) {
        Optional<CourseResponse> foundCourse = courseRepository.findById(id).map(mapper::toResponse);

        if (foundCourse.isPresent()) {
            return foundCourse.get();
        } else {
            throw new ObjectWithSpecifiedIdNotFoundException("The course with specified id doesn't exist in the database.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findAll(String page) {
        final long itemsCount = courseRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return courseRepository.findAll(PageRequest.of(pageNumber, ITEMS_PER_PAGE, Sort.by("id"))).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findAll() {
        return courseRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(CourseRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        Course c = mapper.toEntity(r);
        courseRepository.update(c.getId(), c.getName(), c.getDescription());
    }

    @Override
    public void editAll(List<CourseRequest> requests) {
        List<CourseRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestDataException e) {
                log.info("The course was deleted from the update list. The course: {} .", r);
            }
        });

        courseRepository.saveAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Course> optionalCourseEntities = courseRepository.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            courseRepository.deleteById(id);

            return true;
        } else {
            log.info("Delete was rejected. There is no courses with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        courseRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findCoursesRelateToDepartment(int departmentId) {
        return courseRepository.findCoursesByDepartmentsId(departmentId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findCoursesNotRelateToDepartment(int departmentId) {
        List<Course> allCourses = courseRepository.findAll();
        List<Course> relateToDepartment = courseRepository.findCoursesByDepartmentsId(departmentId);

        return allCourses.stream()
                .filter(course -> !relateToDepartment.contains(course))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findCoursesRelateToTeacher(int teacherId) {
        return courseRepository.findCoursesByTeachersId(teacherId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> findCoursesRelateToDepartmentNotRelateToTeacher(int departmentId, int teacherId) {
        List<Course> coursesRelateToDepartment = courseRepository.findCoursesByDepartmentsId(departmentId);
        List<Course> coursesRelateToTeacher = courseRepository.findCoursesByTeachersId(teacherId);

        return coursesRelateToDepartment.stream()
                .filter(c -> !coursesRelateToTeacher.contains(c))
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void subscribeCourseToDepartment(int departmentId, int courseId) {
        departmentRepository.addCourseToDepartment(departmentId, courseId);
        log.info("Course with id = {} have been subscribed to department with id = {}",
                courseId, departmentId);
    }

    @Override
    public void unsubscribeCourseFromDepartment(int departmentId, int courseId) {
        departmentRepository.deleteCourseFromDepartment(departmentId, courseId);
        log.info("Course with id = {} have been unsubscribed from department with id = {}",
                courseId, departmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public int lastPageNumber() {
        return (int) Math.ceil((double) courseRepository.count() / ITEMS_PER_PAGE);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        departmentRepository.unbindDepartmentsFromCourse(id);
        lessonRepository.unbindLessonsFromCourse(id);
        teacherRepository.unbindTeachersFromCourse(id);
    }

}
