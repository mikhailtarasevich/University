package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Teacher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TeacherRepository extends UserRepository<Teacher> {

    List<Teacher> findTeachersByGroupsId(int groupId);

    List<Teacher> findTeachersByCoursesId(int courseId);

    List<Teacher> findTeachersByDepartmentId(int departmentId);

    @Override
    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.password = :password WHERE t.id = :id")
    void updateUserPassword(@Param("id") int id, @Param("password") String password);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO user_groups (user_id, group_id) VALUES (:id, :groupId)")
    void addUserToGroup(@Param("id") int id, @Param("groupId") int groupId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO user_courses (user_id, course_id) VALUES (:teacherId, :courseId)")
    void addTeacherToCourse(@Param("teacherId") int teacherId, @Param("courseId") Integer courseId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM user_courses WHERE user_id = :teacherId AND course_id = :courseId")
    void deleteTeacherFromCourse(@Param("teacherId") int teacherId, @Param("courseId") int courseId);

    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.department.id = :newDepartmentId WHERE t.id = :teacherId")
    void changeDepartment(@Param("teacherId") int teacherId, @Param("newDepartmentId") Integer newDepartmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.teacherTitle.id = :newTeacherTitleId WHERE t.id = :teacherId")
    void changeTeacherTitle(@Param("teacherId") int teacherId, @Param("newTeacherTitleId") Integer newTeacherTitleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM user_groups WHERE user_id = :teacherId AND group_id = :groupId")
    void deleteTeacherFromGroup(@Param("teacherId") int teacherId, @Param("groupId") int groupId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM user_courses WHERE course_id = :courseId")
    void unbindTeachersFromCourse(@Param("courseId") int courseId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM user_groups WHERE group_id = :groupId")
    void unbindTeachersFromGroup(@Param("groupId") int groupId);

    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.department.id = NULL WHERE t.department.id = :departmentId")
    void unbindTeachersFromDepartment(@Param("departmentId") int departmentId);

    @Modifying
    @Transactional
    @Query("UPDATE Teacher t SET t.teacherTitle.id = NULL WHERE t.teacherTitle.id = :teacherTitleId")
    void unbindTeachersFromTeacherTitle(@Param("teacherTitleId") int teacherTitleId);

}
