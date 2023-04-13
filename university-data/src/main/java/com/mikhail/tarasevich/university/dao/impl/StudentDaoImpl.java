package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;


@Repository
public class StudentDaoImpl extends AbstractUserDaoImpl<Student>
        implements StudentDao {

    private static final String FIND_STUDENTS_RELATE_TO_GROUP_QUERY =
            "SELECT s FROM Student s LEFT JOIN s.group g WHERE g.id = :groupId";

    @Autowired
    public StudentDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Student.class);
    }

    @Override
    public List<Student> findStudentsRelateToGroup(int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_STUDENTS_RELATE_TO_GROUP_QUERY);
        query.setParameter("groupId", groupId);

        return query.getResultList();
    }

    @Override
    public void deleteStudentFromGroup(int userId) {
        Session session = sessionFactory.getCurrentSession();
        Student student = session.get(clazz, userId);
        student.setGroup(null);
    }

    @Override
    public void unbindStudentsFromGroup(int groupId) {
        List<Student> studentsRelateToGroup = findStudentsRelateToGroup(groupId);
        studentsRelateToGroup.forEach(s -> s.setGroup(null));
    }

    @Override
    public void addUserToGroup(int userId, int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Student student = session.get(clazz, userId);
        Group group = session.get(Group.class, groupId);
        student.setGroup(group);
    }

}
