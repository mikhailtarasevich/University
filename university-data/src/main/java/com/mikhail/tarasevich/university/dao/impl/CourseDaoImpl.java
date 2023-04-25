package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.entity.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional
public class CourseDaoImpl extends AbstractPageableCrudDaoImpl<Course>
        implements CourseDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String FIND_COURSES_RELATE_TO_DEPARTMENT_QUERY =
            "SELECT c FROM Course c LEFT JOIN c.departments d WHERE d.id = :departmentId";
    private static final String FIND_COURSES_RELATE_TO_TEACHER_QUERY =
            "SELECT c FROM Course c LEFT JOIN c.teachers t WHERE t.id = :teacherId";
    private static final String UNBIND_COURSES_FROM_TEACHER_QUERY =
            "DELETE FROM user_courses WHERE user_id = :teacherId";
    private static final String UNBIND_COURSES_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_courses WHERE department_id = :departmentId";

    @Autowired
    public CourseDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Course.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesRelateToDepartment(int departmentId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_COURSES_RELATE_TO_DEPARTMENT_QUERY);
        query.setParameter("departmentId", departmentId);

        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesRelateToTeacher(int teacherId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_COURSES_RELATE_TO_TEACHER_QUERY);
        query.setParameter("teacherId", teacherId);

        return query.getResultList();
    }

    @Override
    public void unbindCoursesFromDepartment(int departmentId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_COURSES_FROM_DEPARTMENT_QUERY)
                .setParameter("departmentId", departmentId)
                .executeUpdate();
    }

    @Override
    public void unbindCoursesFromTeacher(int teacherId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_COURSES_FROM_TEACHER_QUERY)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
    }

}
