package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dto.StudentRequest;
import com.mikhail.tarasevich.university.dto.StudentResponse;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.mapper.StudentMapper;
import com.mikhail.tarasevich.university.service.StudentService;
import com.mikhail.tarasevich.university.validator.UserValidator;
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
public class StudentServiceImpl
        extends AbstractUserPageableService<StudentDao, StudentRequest, StudentResponse, Student>
        implements StudentService {

    private final GroupDao groupDao;

    @Autowired
    public StudentServiceImpl(StudentDao userDao, PasswordEncoder encoder,
                              StudentMapper mapper,
                              UserValidator<StudentRequest> validator,
                              GroupDao groupDao) {
        super(userDao, encoder, mapper, validator);
        this.groupDao = groupDao;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Student> optionalStudentEntity = userDao.findById(id);

        if (optionalStudentEntity.isPresent()) {
            groupDao.unbindGroupsFromStudent(id);
            return userDao.deleteById(id);
        } else {
            LOG.info("Delete was rejected. There is no student with specified id in the database. Id = {}", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        ids.forEach(groupDao::unbindGroupsFromStudent);

        boolean result = userDao.deleteByIds(ids);

        if (result) LOG.info("Students have been deleted. Deleted students: {}", ids);
        else LOG.info("Students haven't been deleted. Student ids: {}", ids);

        return result;
    }

    @Override
    public void subscribeUserToGroup(int userId, int groupId) {
        userDao.addUserToGroup(userId, groupId);
        LOG.info("Student with id = {} have been subscribed to group with id = {}", userId, groupId);
    }

    @Override
    public void unsubscribeStudentFromGroup(int userId) {
        userDao.deleteStudentFromGroup(userId);
        LOG.info("Student with id = {} have been unsubscribed from group", userId);
    }

    @Override
    public List<StudentResponse> findStudentsRelateToGroup(int groupId) {
        return userDao.findStudentsRelateToGroup(groupId)
                .stream().map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

}
