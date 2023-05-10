package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentRepository extends UserRepository<Student> {

    @Override
    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.password = :password WHERE s.id = :id")
    void updateUserPassword(@Param("id") int id, @Param("password") String password);

    List<Student> findStudentsByGroupId(int groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.group.id = :groupId WHERE s.id = :id")
    void addUserToGroup(@Param("id") int id, @Param("groupId") int groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.group.id = NULL WHERE s.group.id = :groupId")
    void unbindStudentsFromGroup(@Param("groupId") int groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.group.id = NULL WHERE s.id = :userId")
    void deleteStudentFromGroup(@Param("userId") int userId);

}
