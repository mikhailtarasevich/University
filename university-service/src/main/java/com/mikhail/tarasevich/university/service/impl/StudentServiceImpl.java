package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.mapper.StudentMapper;
import com.mikhail.tarasevich.university.repository.GroupRepository;
import com.mikhail.tarasevich.university.repository.RoleRepository;
import com.mikhail.tarasevich.university.repository.StudentRepository;
import com.mikhail.tarasevich.university.service.StudentService;
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
public class StudentServiceImpl
        extends AbstractUserPageableService<StudentRepository, StudentRequest, StudentResponse, Student>
        implements StudentService {

    private final GroupRepository groupRepository;

    public StudentServiceImpl(StudentRepository studentRepository, RoleRepository roleRepository,
                              PasswordEncoder encoder, StudentMapper mapper,
                              UserValidator<StudentRequest> validator,
                              GroupRepository groupRepository) {
        super(studentRepository, roleRepository, encoder, mapper, validator);
        this.groupRepository = groupRepository;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Student> optionalStudentEntity = userRepository.findById(id);

        if (optionalStudentEntity.isPresent()) {
            groupRepository.unbindGroupsFromStudent(id);
            roleRepository.unbindRoleFromUser(id);
            userRepository.deleteById(id);
            return true;
        } else {
            log.info("Delete was rejected. There is no student with specified id in the database. Id = {}",
                    id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(groupRepository::unbindGroupsFromStudent);
        ids.forEach(roleRepository::unbindRoleFromUser);

        userRepository.deleteAllByIdInBatch(ids);

        return true;
    }

    @Override
    public void subscribeUserToGroup(int userId, int groupId) {
        userRepository.addUserToGroup(userId, groupId);
        log.info("Student with id = {} have been subscribed to group with id = {}", userId, groupId);
    }

    @Override
    public void unsubscribeStudentFromGroup(int userId) {
        userRepository.deleteStudentFromGroup(userId);
        log.info("Student with id = {} have been unsubscribed from group", userId);
    }

    @Override
    public List<StudentResponse> findStudentsRelateToGroup(int groupId) {
        return userRepository.findStudentsByGroupId(groupId)
                .stream().map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentResponse> findStudentsNotRelateToGroup(int groupId) {
        List<Student> allStudents = userRepository.findAll();
        List<Student> relateToGroup = userRepository.findStudentsByGroupId(groupId);

        return allStudents.stream()
                .filter(student -> !relateToGroup.contains(student))
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

}
