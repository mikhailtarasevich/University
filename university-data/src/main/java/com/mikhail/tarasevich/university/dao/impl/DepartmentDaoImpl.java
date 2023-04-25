package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.entity.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDaoImpl extends AbstractPageableCrudDaoImpl<Department>
        implements DepartmentDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ADD_COURSE_TO_DEPARTMENT_QUERY =
            "INSERT INTO department_courses (department_id, course_id) VALUES (:departmentId, :courseId)";
    private static final String DELETE_COURSE_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_courses WHERE department_id = :departmentId AND course_id = :courseId";
    private static final String UNBIND_DEPARTMENTS_FROM_COURSE_QUERY =
            "DELETE FROM department_courses WHERE course_id = :courseId";

    @Autowired
    public DepartmentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Department.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    public void addCourseToDepartment(int departmentId, int courseId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(ADD_COURSE_TO_DEPARTMENT_QUERY)
                .setParameter("departmentId", departmentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public void deleteCourseFromDepartment(int departmentId, int courseId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(DELETE_COURSE_FROM_DEPARTMENT_QUERY)
                .setParameter("departmentId", departmentId)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

    @Override
    public void unbindDepartmentsFromCourse(int courseId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_DEPARTMENTS_FROM_COURSE_QUERY)
                .setParameter("courseId", courseId)
                .executeUpdate();
    }

}
