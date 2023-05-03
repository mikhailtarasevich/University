package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Student;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public class StudentDaoImpl extends AbstractUserDaoImpl<Student>
        implements StudentDao {

    private static final String FIND_STUDENTS_RELATE_TO_GROUP_QUERY =
            "SELECT s FROM Student s LEFT JOIN s.group g WHERE g.id = :groupId";

    public StudentDaoImpl(EntityManager entityManager) {
        super(entityManager, Student.class);
    }

    @Override
    public List<Student> findStudentsRelateToGroup(int groupId) {
        Query query = entityManager.createQuery(FIND_STUDENTS_RELATE_TO_GROUP_QUERY);
        query.setParameter("groupId", groupId);

        return query.getResultList();
    }

    @Override
    public void deleteStudentFromGroup(int userId) {
        Student student = entityManager.find(clazz, userId);
        student.setGroup(null);
    }

    @Override
    public void unbindStudentsFromGroup(int groupId) {
        List<Student> studentsRelateToGroup = findStudentsRelateToGroup(groupId);
        studentsRelateToGroup.forEach(s -> s.setGroup(null));
    }

    @Override
    public void addUserToGroup(int userId, int groupId) {
        Student student = entityManager.find(clazz, userId);
        Group group = entityManager.find(Group.class, groupId);
        student.setGroup(group);
    }

}
