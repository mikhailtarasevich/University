package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class GroupDaoImpl extends AbstractPageableCrudDaoImpl<Group> implements GroupDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String UPDATE_FACULTY_QUERY =
            "UPDATE groups SET faculty_id = :facultyId WHERE id = :groupId";
    private static final String UPDATE_HEAD_USER_QUERY =
            "UPDATE groups SET head_user_id = :studentId WHERE id = :groupId";
    private static final String UPDATE_EDUCATION_FORM_QUERY =
            "UPDATE groups SET education_form_id = :educationFormId WHERE id = :groupId";
    private static final String UNBIND_GROUPS_FROM_STUDENT_QUERY =
            "UPDATE groups SET head_user_id = NULL WHERE head_user_id = :studentId";
    private static final String UNBIND_GROUPS_FROM_TEACHER_QUERY =
            "DELETE FROM user_groups WHERE user_id = :teacherId";
    private static final String UNBIND_GROUPS_FROM_EDUCATION_FORM_QUERY =
            "UPDATE groups SET education_form_id = NULL WHERE education_form_id = :educationFormId";
    private static final String UNBIND_GROUPS_FROM_FACULTY_QUERY =
            "UPDATE groups SET faculty_id = NULL WHERE faculty_id = :facultyId";

    @Autowired
    public GroupDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Group.class, UNIQUE_NAME_PARAMETER);
    }

    @Override
    public void changeFaculty(int groupId, int newFacultyId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_FACULTY_QUERY)
                .setParameter("facultyId", newFacultyId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void changeHeadUser(int groupId, int newUserId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_HEAD_USER_QUERY)
                .setParameter("studentId", newUserId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void changeEducationForm(int groupId, int newEducationFormId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UPDATE_EDUCATION_FORM_QUERY)
                .setParameter("educationFormId", newEducationFormId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findGroupsRelateToTeacher(int teacherId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(Teacher.class, teacherId);
        List<Group> groups = teacher.getGroups();

        return groups;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> findGroupsNotRelateToTeacher(int teacherId) {
        Session session = sessionFactory.getCurrentSession();
        List<Group> groups = session.createQuery("FROM Group").getResultList();
        Teacher teacher = session.get(Teacher.class, teacherId);
        groups.removeIf(g -> teacher.getGroups().contains(g));

        return groups;
    }

    @Override
    public void unbindGroupsFromStudent(int studentId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_GROUPS_FROM_STUDENT_QUERY)
                .setParameter("studentId", studentId)
                .executeUpdate();
    }

    @Override
    public void unbindGroupsFromTeacher(int teacherId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_GROUPS_FROM_TEACHER_QUERY)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
    }

    @Override
    public void unbindGroupsFromEducationForm(int educationFormId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_GROUPS_FROM_EDUCATION_FORM_QUERY)
                .setParameter("educationFormId", educationFormId)
                .executeUpdate();
    }

    @Override
    public void unbindGroupsFromFaculty(int facultyId) {
        Session session = sessionFactory.getCurrentSession();
        session.createNativeQuery(UNBIND_GROUPS_FROM_FACULTY_QUERY)
                .setParameter("facultyId", facultyId)
                .executeUpdate();
    }

}
