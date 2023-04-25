package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Teacher;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class GroupDaoImpl extends AbstractPageableCrudDaoImpl<Group> implements GroupDao {

    private static final String UNIQUE_NAME_PARAMETER = "name";
    private static final String ORDER_BY_QUERY = "id";
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

    public GroupDaoImpl(EntityManager entityManager) {
        super(entityManager, Group.class, UNIQUE_NAME_PARAMETER, ORDER_BY_QUERY);
    }

    @Override
    public void changeFaculty(int groupId, int newFacultyId) {
        entityManager.createNativeQuery(UPDATE_FACULTY_QUERY)
                .setParameter("facultyId", newFacultyId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void changeHeadUser(int groupId, int newUserId) {
        entityManager.createNativeQuery(UPDATE_HEAD_USER_QUERY)
                .setParameter("studentId", newUserId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public void changeEducationForm(int groupId, int newEducationFormId) {
        entityManager.createNativeQuery(UPDATE_EDUCATION_FORM_QUERY)
                .setParameter("educationFormId", newEducationFormId)
                .setParameter("groupId", groupId)
                .executeUpdate();
    }

    @Override
    public List<Group> findGroupsRelateToTeacher(int teacherId) {
        Teacher teacher = entityManager.find(Teacher.class, teacherId);

        return teacher.getGroups();
    }

    @Override
    public List<Group> findGroupsNotRelateToTeacher(int teacherId) {
        List<Group> groups = entityManager.createQuery("FROM Group").getResultList();
        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        groups.removeIf(g -> teacher.getGroups().contains(g));

        return groups;
    }

    @Override
    public void unbindGroupsFromStudent(int studentId) {
        entityManager.createNativeQuery(UNBIND_GROUPS_FROM_STUDENT_QUERY)
                .setParameter("studentId", studentId)
                .executeUpdate();
    }

    @Override
    public void unbindGroupsFromTeacher(int teacherId) {
        entityManager.createNativeQuery(UNBIND_GROUPS_FROM_TEACHER_QUERY)
                .setParameter("teacherId", teacherId)
                .executeUpdate();
    }

    @Override
    public void unbindGroupsFromEducationForm(int educationFormId) {
        entityManager.createNativeQuery(UNBIND_GROUPS_FROM_EDUCATION_FORM_QUERY)
                .setParameter("educationFormId", educationFormId)
                .executeUpdate();
    }

    @Override
    public void unbindGroupsFromFaculty(int facultyId) {
        entityManager.createNativeQuery(UNBIND_GROUPS_FROM_FACULTY_QUERY)
                .setParameter("facultyId", facultyId)
                .executeUpdate();
    }

}
