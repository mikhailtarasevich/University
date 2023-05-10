package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Department d SET d.name = :name, d.description = :description WHERE d.id = :id")
    void update(@Param("id") int id, @Param("name") String name, @Param("description") String description);

    @Modifying
    @Transactional
    @Query("DELETE FROM Department WHERE id IN (SELECT d.id FROM Department d JOIN d.courses c WHERE c.id = :courseId)")
    void unbindDepartmentsFromCourse(@Param("courseId") int courseId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM department_courses dc WHERE dc.department_id = :departmentId AND dc.course_id = :courseId")
    void deleteCourseFromDepartment(@Param("departmentId") int departmentId, @Param("courseId") int courseId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO department_courses (department_id, course_id) VALUES (:departmentId, :courseId)")
    void addCourseToDepartment(@Param("departmentId") int departmentId, @Param("courseId") int courseId);

}
