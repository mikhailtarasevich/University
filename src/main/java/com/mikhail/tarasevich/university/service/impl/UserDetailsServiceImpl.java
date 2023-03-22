package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.dao.PrivilegeDao;
import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.entity.Privilege;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.security.UserSecurityDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
public class UserDetailsServiceImpl implements UserDetailsService {

    private final StudentDao studentDao;
    private final TeacherDao teacherDao;
    private final PrivilegeDao privilegeDao;

    @Autowired
    public UserDetailsServiceImpl(StudentDao studentDao, TeacherDao teacherDao, PrivilegeDao privilegeDao) {
        this.studentDao = studentDao;
        this.teacherDao = teacherDao;
        this.privilegeDao = privilegeDao;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> studentOptional = studentDao.findByName(email);
        Optional<Teacher> teacherOptional = teacherDao.findByName(email);

        if (studentOptional.isPresent()) {
            log.info("During the identity verification process, the system found the student with email {} in the database.",
                    studentOptional.get().getEmail());
            return getStudentDetails(studentOptional.get());
        } else if (teacherOptional.isPresent()) {
            log.info("During the identity verification process, the system found the teacher with email {} in the database.",
                    teacherOptional.get().getEmail());
            return getTeacherDetails(teacherOptional.get());
        } else {
            throw new UsernameNotFoundException("There is no user with this email");
        }
    }

    private UserDetails getStudentDetails(Student student) {
        return new UserSecurityDetails(student, privilegeDao.findPrivilegesRelateToUser(student.getEmail()).stream()
                .map(Privilege::getName)
                .collect(Collectors.toList()));
    }

    private UserDetails getTeacherDetails(Teacher teacher) {
        return new UserSecurityDetails(teacher, privilegeDao.findPrivilegesRelateToUser(teacher.getEmail()).stream()
                .map(Privilege::getName)
                .collect(Collectors.toList()));
    }

}
