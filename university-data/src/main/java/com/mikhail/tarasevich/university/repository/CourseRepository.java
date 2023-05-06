package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByName(String name);

    List<Course> findCoursesByDepartmentsId(int departmentId);

    List<Course> findCoursesByTeachersId(int teacherId);

    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.name = :name, c.description = :description WHERE c.id = :id")
    void update(@Param("id") int id, @Param("name") String name, @Param("description") String description);

    @Modifying
    @Transactional
    @Query("DELETE FROM Course WHERE id IN (SELECT c.id FROM Course c JOIN c.departments d WHERE d.id = :departmentId)")
    void unbindCoursesFromDepartment(@Param("departmentId")int departmentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Course WHERE id IN (SELECT c.id FROM Course c JOIN c.teachers t WHERE t.id = :teacherId)")
    void unbindCoursesFromTeacher(@Param("teacherId")int teacherId);

}
