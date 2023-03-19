package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.LessonDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.dto.GroupResponse;
import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.mapper.TeacherMapper;
import com.mikhail.tarasevich.university.service.TeacherService;
import com.mikhail.tarasevich.university.validator.UserValidator;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
public class TeacherServiceImpl
        extends AbstractUserPageableService<TeacherDao, TeacherRequest, TeacherResponse, Teacher>
        implements TeacherService {

    private final LessonDao lessonDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;

    @Autowired
    public TeacherServiceImpl(TeacherDao userDao, PasswordEncoder passwordEncoder, TeacherMapper userMapper,
                              UserValidator<TeacherRequest> validator,
                              LessonDao lessonDao, CourseDao courseDao, GroupDao groupDao) {
        super(userDao, passwordEncoder, userMapper, validator);
        this.lessonDao = lessonDao;
        this.courseDao = courseDao;
        this.groupDao = groupDao;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Teacher> optionalStudentEntity = userDao.findById(id);

        if (optionalStudentEntity.isPresent()) {
            lessonDao.unbindLessonsFromTeacher(id);
            courseDao.unbindCoursesFromTeacher(id);
            groupDao.unbindGroupsFromTeacher(id);
            return userDao.deleteById(id);
        } else {
            log.info("Delete was rejected. There is no teacher with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(lessonDao::unbindLessonsFromTeacher);
        ids.forEach(courseDao::unbindCoursesFromTeacher);
        ids.forEach(groupDao::unbindGroupsFromTeacher);

        boolean result = userDao.deleteByIds(ids);

        if (result) log.info("Teachers have been deleted. Deleted teachers: {}", ids);
        else log.info("Teachers haven't been deleted. Teachers ids: {}", ids);

        return result;
    }

    @Override
    public void changeTeacherTeacherTitle(int teacherId, int teacherTitleId) {
        userDao.changeTeacherTitle(teacherId, teacherTitleId);
        log.info("Teacher title with id = {} have been set to teacher with id = {}", teacherTitleId, teacherId);
    }

    @Override
    public void changeTeacherDepartment(int teacherId, int departmentId) {
        userDao.changeDepartment(teacherId, departmentId);
        log.info("Department with id = {} have been set to teacher with id = {}", departmentId, teacherId);
    }

    @Override
    public void subscribeUserToGroup(int teacherId, int groupId) {
        userDao.addUserToGroup(teacherId, groupId);
        log.info("Teacher with id = {} have been subscribed to group with id = {}", teacherId, groupId);
    }

    @Override
    public void subscribeTeacherToGroups(int teacherId, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            groupIds.forEach(groupId -> userDao.addUserToGroup(teacherId, groupId));
            log.info("Teacher with id = {} have been subscribed to groups with ids = {}", teacherId, groupIds);
        }
    }

    @Override
    public void unsubscribeTeacherFromGroups(int teacherId, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            groupIds.forEach(groupId -> userDao.deleteTeacherFromGroup(teacherId, groupId));
            log.info("Teacher with id = {} have been unsubscribed from groups with ids = {}", teacherId, groupIds);
        }
    }

    @Override
    public void subscribeTeacherToCourse(int teacherId, int courseId) {
        userDao.addTeacherToCourse(teacherId, courseId);
        log.info("Teacher with id = {} have been subscribed to course with id = {}", teacherId, courseId);
    }

    @Override
    public void unsubscribeTeacherFromCourse(int teacherId, int courseId) {
        userDao.deleteTeacherFromCourse(teacherId, courseId);
        log.info("Teacher with id = {} have been unsubscribed from course with id = {}", teacherId, courseId);
    }

    @Override
    public void subscribeTeacherToCourses(int teacherId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> userDao.addTeacherToCourse(teacherId, courseId));
        log.info("Teacher with id = {} have been subscribed to courses with id = {}", teacherId, courseIds);
    }

    @Override
    public void unsubscribeTeacherFromCourses(int teacherId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> userDao.deleteTeacherFromCourse(teacherId, courseId));
        log.info("Teacher with id = {} have been unsubscribed from courses with id = {}", teacherId, courseIds);
    }

    @Override
    public List<TeacherResponse> findTeachersRelateToGroup(int groupId) {
        return userDao.findTeachersRelateToGroup(groupId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherResponse> findTeachersRelateToCourse(int courseId) {
        return userDao.findTeachersRelateToCourse(courseId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherResponse> findTeachersRelateToDepartment(int departmentId) {
        return userDao.findTeachersRelateToDepartment(departmentId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeacherResponse> findTeachersRelateToTeacherTitle(int teacherTitleId) {
        return userDao.findAll().stream()
                .filter(t-> t.getTeacherTitle().getId() == teacherTitleId)
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

}
