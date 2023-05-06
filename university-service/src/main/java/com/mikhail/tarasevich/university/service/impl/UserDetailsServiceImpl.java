package com.mikhail.tarasevich.university.service.impl;

import com.mikhail.tarasevich.university.entity.Privilege;
import com.mikhail.tarasevich.university.entity.Student;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.repository.PrivilegeRepository;
import com.mikhail.tarasevich.university.repository.StudentRepository;
import com.mikhail.tarasevich.university.repository.TeacherRepository;
import com.mikhail.tarasevich.university.security.UserSecurityDetails;
import lombok.extern.log4j.Log4j2;
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

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PrivilegeRepository privilegeRepository;

    public UserDetailsServiceImpl(StudentRepository studentRepository, TeacherRepository teacherRepository,
                                  PrivilegeRepository privilegeRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Student> studentOptional = studentRepository.findByEmail(email);
        Optional<Teacher> teacherOptional = teacherRepository.findByEmail(email);

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
        return new UserSecurityDetails(student, privilegeRepository.findPrivilegesRelateToUser(student.getEmail()).stream()
                .map(Privilege::getName)
                .collect(Collectors.toList()));
    }

    private UserDetails getTeacherDetails(Teacher teacher) {
        return new UserSecurityDetails(teacher, privilegeRepository.findPrivilegesRelateToUser(teacher.getEmail()).stream()
                .map(Privilege::getName)
                .collect(Collectors.toList()));
    }

}
