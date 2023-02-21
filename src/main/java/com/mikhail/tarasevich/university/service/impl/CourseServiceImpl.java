package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.*;
import com.mikhail.tarasevich.university.dto.CourseRequest;
import com.mikhail.tarasevich.university.dto.CourseResponse;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.exception.IncorrectRequestData;
import com.mikhail.tarasevich.university.mapper.CourseMapper;
import com.mikhail.tarasevich.university.service.CourseService;
import com.mikhail.tarasevich.university.validator.CourseValidator;
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
public class CourseServiceImpl extends AbstractPageableService implements CourseService {


    private final CourseDao courseDao;
    private final DepartmentDao departmentDao;
    private final LessonDao lessonDao;
    private final TeacherDao teacherDao;
    private final CourseMapper mapper;
    private final CourseValidator validator;


    @Override
    public CourseResponse register(CourseRequest r) {
        validator.validateUniqueNameInDB(r);
        validator.validateNameNotNullOrEmpty(r);

        return mapper.toResponse(courseDao.save(mapper.toEntity(r)));
    }

    @Override
    public void registerAll(List<CourseRequest> requests) {
        List<CourseRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateUniqueNameInDB(r);
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                log.info("The request were deleted from the save list. Request: {} .", r);
            }
        });

        courseDao.saveAll(acceptableRequests.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList()));
        log.info("Courses were saved in the database. Saved courses: {} .", acceptableRequests);
    }

    @Override
    public Optional<CourseResponse> findById(int id) {
        return courseDao.findById(id).map(mapper::toResponse);
    }

    @Override
    public List<CourseResponse> findAll(String page) {
        final long itemsCount = courseDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return courseDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(CourseRequest r) {
        validator.validateNameNotNullOrEmpty(r);
        courseDao.update(mapper.toEntity(r));
    }

    @Override
    public void editAll(List<CourseRequest> requests) {
        List<CourseRequest> acceptableRequests = new ArrayList<>();

        requests.forEach(r -> {
            try {
                validator.validateNameNotNullOrEmpty(r);
                acceptableRequests.add(r);
            } catch (IncorrectRequestData e) {
                log.info("The course was deleted from the update list. The course: {} .", r);
            }
        });

        courseDao.updateAll(
                acceptableRequests.stream()
                        .map(mapper::toEntity)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Course> optionalCourseEntities = courseDao.findById(id);

        if (optionalCourseEntities.isPresent()) {

            unbindDependenciesBeforeDelete(id);

            return courseDao.deleteById(id);
        } else {
            log.info("Delete was rejected. There is no courses with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(this::unbindDependenciesBeforeDelete);

        boolean result = courseDao.deleteByIds(ids);

        if (result) log.info("Courses have been deleted. Deleted courses: {}", ids);

        return result;
    }

    @Override
    public List<CourseResponse> findCoursesRelateToDepartment(int departmentId) {
        return courseDao.findCoursesRelateToDepartment(departmentId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> findCoursesRelateToTeacher(int teacherId) {
        return courseDao.findCoursesRelateToTeacher(teacherId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void subscribeCourseToDepartment(int departmentId, int courseId) {
        departmentDao.addCourseToDepartment(departmentId, courseId);
        log.info("Course with id = {} have been subscribed to department with id = {}",
                courseId, departmentId);
    }

    @Override
    public void unsubscribeCourseFromDepartment(int departmentId, int courseId) {
        departmentDao.deleteCourseFromDepartment(departmentId, courseId);
        log.info("Course with id = {} have been unsubscribed from department with id = {}",
                courseId, departmentId);
    }

    private void unbindDependenciesBeforeDelete(int id) {
        departmentDao.unbindDepartmentsFromCourse(id);
        lessonDao.unbindLessonsFromCourse(id);
        teacherDao.unbindTeachersFromCourse(id);
    }

}
