package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.entity.Course;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CourseDaoImpl extends AbstractPageableCrudDaoImpl<Course>
        implements CourseDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";
    private static final String FIND_COURSES_RELATE_TO_DEPARTMENT_QUERY =
            "SELECT c FROM Course c LEFT JOIN c.departments d WHERE d.id = :departmentId";
    private static final String FIND_COURSES_RELATE_TO_TEACHER_QUERY =
            "SELECT c FROM Course c LEFT JOIN c.teachers t WHERE t.id = :teacherId";
    private static final String UNBIND_COURSES_FROM_TEACHER_QUERY =
            "DELETE FROM user_courses WHERE user_id = :teacherId";
    private static final String UNBIND_COURSES_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_courses WHERE department_id = :departmentId";

    public CourseDaoImpl(EntityManager entityManager) {
        super(entityManager, Course.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

    @Override
    public List<Course> findCoursesRelateToDepartment(int departmentId) {
        Query query = entityManager.createQuery(FIND_COURSES_RELATE_TO_DEPARTMENT_QUERY);
        query.setParameter("departmentId", departmentId);

        return query.getResultList();
    }

    @Override
    public List<Course> findCoursesRelateToTeacher(int teacherId) {
        Query query = entityManager.createQuery(FIND_COURSES_RELATE_TO_TEACHER_QUERY);
        query.setParameter("teacherId", teacherId);

        return query.getResultList();
    }

    @Override
    public void unbindCoursesFromDepartment(int departmentId) {
        entityManager.createNativeQuery(UNBIND_COURSES_FROM_DEPARTMENT_QUERY)
                .setParameter("departmentId", departmentId)
                .executeUpdate();
    }

    @Override
    public void unbindCoursesFromTeacher(int teacherId) {
        entityManager.createNativeQuery(UNBIND_COURSES_FROM_TEACHER_QUERY)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
    }

}
