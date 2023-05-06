package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.TeacherRequest;
import com.mikhail.tarasevich.university.dto.TeacherResponse;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.mapper.TeacherMapper;
import com.mikhail.tarasevich.university.repository.CourseRepository;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.repository.LessonRepository;
import com.mikhail.tarasevich.university.repository.RoleRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.service.TeacherService;
import com.mikhail.tarasevich.university.service.validator.UserValidator;
import lombok.extern.log4j.Log4j2;
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
        extends AbstractUserPageableService<TeacherRepository, TeacherRequest, TeacherResponse, Teacher>
        implements TeacherService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;

    public TeacherServiceImpl(TeacherRepository userRepository, RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder, TeacherMapper teacherMapper,
                              UserValidator<TeacherRequest> validator, LessonRepository lessonRepository,
                              CourseRepository courseRepository, GroupRepository groupRepository) {
        super(userRepository, roleRepository, passwordEncoder, teacherMapper, validator);
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Teacher> optionalStudentEntity = userRepository.findById(id);

        if (optionalStudentEntity.isPresent()) {
            lessonRepository.unbindLessonsFromTeacher(id);
            courseRepository.unbindCoursesFromTeacher(id);
            groupRepository.unbindGroupsFromTeacher(id);
            roleRepository.unbindRoleFromUser(id);
            userRepository.deleteById(id);
            return true;
        } else {
            log.info("Delete was rejected. There is no teacher with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(lessonRepository::unbindLessonsFromTeacher);
        ids.forEach(courseRepository::unbindCoursesFromTeacher);
        ids.forEach(groupRepository::unbindGroupsFromTeacher);
        ids.forEach(roleRepository::unbindRoleFromUser);

        userRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    public void changeTeacherTeacherTitle(int teacherId, int teacherTitleId) {
        userRepository.changeTeacherTitle(teacherId, teacherTitleId);
        log.info("Teacher title with id = {} have been set to teacher with id = {}", teacherTitleId, teacherId);
    }

    @Override
    public void changeTeacherDepartment(int teacherId, int departmentId) {
        userRepository.changeDepartment(teacherId, departmentId);
        log.info("Department with id = {} have been set to teacher with id = {}", departmentId, teacherId);
    }

    @Override
    public void subscribeUserToGroup(int teacherId, int groupId) {
        userRepository.addUserToGroup(teacherId, groupId);
        log.info("Teacher with id = {} have been subscribed to group with id = {}", teacherId, groupId);
    }

    @Override
    public void subscribeTeacherToGroups(int teacherId, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            groupIds.forEach(groupId -> userRepository.addUserToGroup(teacherId, groupId));
            log.info("Teacher with id = {} have been subscribed to groups with ids = {}", teacherId, groupIds);
        }
    }

    @Override
    public void unsubscribeTeacherFromGroups(int teacherId, List<Integer> groupIds) {
        if (!groupIds.isEmpty()) {
            groupIds.forEach(groupId -> userRepository.deleteTeacherFromGroup(teacherId, groupId));
            log.info("Teacher with id = {} have been unsubscribed from groups with ids = {}", teacherId, groupIds);
        }
    }

    @Override
    public void subscribeTeacherToCourse(int teacherId, int courseId) {
        userRepository.addTeacherToCourse(teacherId, courseId);
        log.info("Teacher with id = {} have been subscribed to course with id = {}", teacherId, courseId);
    }

    @Override
    public void unsubscribeTeacherFromCourse(int teacherId, int courseId) {
        userRepository.deleteTeacherFromCourse(teacherId, courseId);
        log.info("Teacher with id = {} have been unsubscribed from course with id = {}", teacherId, courseId);
    }

    @Override
    public void subscribeTeacherToCourses(int teacherId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> userRepository.addTeacherToCourse(teacherId, courseId));
        log.info("Teacher with id = {} have been subscribed to courses with id = {}", teacherId, courseIds);
    }

    @Override
    public void unsubscribeTeacherFromCourses(int teacherId, List<Integer> courseIds) {
        courseIds.forEach(courseId -> userRepository.deleteTeacherFromCourse(teacherId, courseId));
        log.info("Teacher with id = {} have been unsubscribed from courses with id = {}", teacherId, courseIds);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> findTeachersRelateToGroup(int groupId) {
        return userRepository.findTeachersByGroupsId(groupId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> findTeachersRelateToCourse(int courseId) {
        return userRepository.findTeachersByCoursesId(courseId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> findTeachersRelateToDepartment(int departmentId) {
        return userRepository.findTeachersByDepartmentId(departmentId).stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherResponse> findTeachersRelateToTeacherTitle(int teacherTitleId) {
        return userRepository.findAll().stream()
                .filter(t -> t.getTeacherTitle() != null && t.getTeacherTitle().getId() == teacherTitleId)
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

}
