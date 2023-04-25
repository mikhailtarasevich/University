package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TeacherDaoImpl extends AbstractUserDaoImpl<Teacher>
        implements TeacherDao {

    private static final String FIND_TEACHERS_RELATE_TO_GROUP_QUERY =
            "SELECT t FROM Teacher t LEFT JOIN t.groups g WHERE g.id = :groupId";
    private static final String FIND_TEACHERS_RELATE_TO_COURSE_QUERY =
            "SELECT t FROM Teacher t LEFT JOIN t.courses c WHERE c.id = :courseId";
    private static final String FIND_TEACHERS_RELATE_TO_DEPARTMENT_QUERY =
            "SELECT t FROM Teacher t LEFT JOIN t.department d WHERE d.id = :departmentId";
    private static final String FIND_TEACHERS_RELATE_TO_TEACHER_TITLE_QUERY =
            "SELECT t FROM Teacher t LEFT JOIN t.teacherTitle tt WHERE tt.id = :teacherTitleId";

    @Autowired
    public TeacherDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Teacher.class);
    }

    @Override
    public void addTeacherToCourse(int teacherId, Integer courseId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(clazz, teacherId);
        Course course = session.get(Course.class, courseId);

        teacher.getCourses().add(course);
        course.getTeachers().add(teacher);
    }

    @Override
    public void deleteTeacherFromCourse(int teacherId, int courseId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(clazz, teacherId);
        teacher.getCourses().forEach(c -> {
            if (c.getId() == courseId) c.getTeachers().remove(teacher);
        });
        teacher.getCourses().removeIf(c -> c.getId() == courseId);
    }

    @Override
    public void changeDepartment(int teacherId, Integer newDepartmentId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(clazz, teacherId);
        Department department = session.get(Department.class, newDepartmentId);
        teacher.setDepartment(department);
    }

    @Override
    public void changeTeacherTitle(int teacherId, Integer newTeacherTitleId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(clazz, teacherId);
        TeacherTitle teacherTitle = session.get(TeacherTitle.class, newTeacherTitleId);
        teacher.setTeacherTitle(teacherTitle);
    }

    @Override
    public void deleteTeacherFromGroup(int userId, int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(clazz, userId);
        teacher.getGroups().removeIf(g -> g.getId() == groupId);
    }

    @Override
    public List<Teacher> findTeachersRelateToGroup(int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_TEACHERS_RELATE_TO_GROUP_QUERY);
        query.setParameter("groupId", groupId);

        return query.getResultList();
    }

    @Override
    public List<Teacher> findTeachersRelateToCourse(int courseId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_TEACHERS_RELATE_TO_COURSE_QUERY);
        query.setParameter("courseId", courseId);

        return query.getResultList();
    }

    @Override
    public List<Teacher> findTeachersRelateToDepartment(int departmentId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_TEACHERS_RELATE_TO_DEPARTMENT_QUERY);
        query.setParameter("departmentId", departmentId);

        return query.getResultList();
    }

    @Override
    public void unbindTeachersFromCourse(int courseId) {
        Session session = sessionFactory.getCurrentSession();
        Course course = session.get(Course.class, courseId);
        course.setTeachers(new ArrayList<>());
    }

    @Override
    public void unbindTeachersFromDepartment(int departmentId) {
        List<Teacher> teachers = findTeachersRelateToDepartment(departmentId);
        teachers.forEach(t -> t.setDepartment(null));
    }

    @Override
    public void unbindTeachersFromGroup(int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Group group = session.get(Group.class, groupId);
        group.setTeachers(new ArrayList<>());
    }

    @Override
    public void unbindTeachersFromTeacherTitle(int teacherTitleId) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery(FIND_TEACHERS_RELATE_TO_TEACHER_TITLE_QUERY);
        query.setParameter("teacherTitleId", teacherTitleId);
        List<Teacher> teachersRelateToTeacherTitle = query.getResultList();
        teachersRelateToTeacherTitle.forEach(t -> t.setTeacherTitle(null));
    }

    @Override
    public void addUserToGroup(int userId, int groupId) {
        Session session = sessionFactory.getCurrentSession();
        Teacher teacher = session.get(clazz, userId);
        Group group = session.get(Group.class, groupId);
        teacher.getGroups().add(group);
    }

}
