package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.entity.Department;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class DepartmentDaoImpl extends AbstractPageableCrudDaoImpl<Department>
        implements DepartmentDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";
    private static final String ADD_COURSE_TO_DEPARTMENT_QUERY =
            "INSERT INTO department_courses (department_id, course_id) VALUES (:departmentId, :courseId)";
    private static final String DELETE_COURSE_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_courses WHERE department_id = :departmentId AND course_id = :courseId";
    private static final String UNBIND_DEPARTMENTS_FROM_COURSE_QUERY =
            "DELETE FROM department_courses WHERE course_id = :courseId";

    public DepartmentDaoImpl(EntityManager entityManager) {
        super(entityManager, Department.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

    @Override
    public void addCourseToDepartment(int departmentId, int courseId) {
        entityManager.createNativeQuery(ADD_COURSE_TO_DEPARTMENT_QUERY)
                .setParameter("departmentId", departmentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public void deleteCourseFromDepartment(int departmentId, int courseId) {
        entityManager.createNativeQuery(DELETE_COURSE_FROM_DEPARTMENT_QUERY)
                .setParameter("departmentId", departmentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public void unbindDepartmentsFromCourse(int courseId) {
        entityManager.createNativeQuery(UNBIND_DEPARTMENTS_FROM_COURSE_QUERY)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

}
