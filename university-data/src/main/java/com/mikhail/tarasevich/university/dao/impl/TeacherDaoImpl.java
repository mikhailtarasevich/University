package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.entity.Course;
import com.mikhail.tarasevich.university.entity.Department;
import com.mikhail.tarasevich.university.entity.Group;
import com.mikhail.tarasevich.university.entity.Teacher;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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

    public TeacherDaoImpl(EntityManager entityManager) {
        super(entityManager, Teacher.class);
    }

    @Override
    public void addTeacherToCourse(int teacherId, Integer courseId) {
        Teacher teacher = entityManager.find(clazz, teacherId);
        Course course = entityManager.find(Course.class, courseId);

        teacher.getCourses().add(course);
        course.getTeachers().add(teacher);
    }

    @Override
    public void deleteTeacherFromCourse(int teacherId, int courseId) {
        Teacher teacher = entityManager.find(clazz, teacherId);
        teacher.getCourses().forEach(c -> {
            if (c.getId() == courseId) c.getTeachers().remove(teacher);
        });
        teacher.getCourses().removeIf(c -> c.getId() == courseId);
    }

    @Override
    public void changeDepartment(int teacherId, Integer newDepartmentId) {
        Teacher teacher = entityManager.find(clazz, teacherId);
        Department department = entityManager.find(Department.class, newDepartmentId);
        teacher.setDepartment(department);
    }

    @Override
    public void changeTeacherTitle(int teacherId, Integer newTeacherTitleId) {
        Teacher teacher = entityManager.find(clazz, teacherId);
        TeacherTitle teacherTitle = entityManager.find(TeacherTitle.class, newTeacherTitleId);
        teacher.setTeacherTitle(teacherTitle);
    }

    @Override
    public void deleteTeacherFromGroup(int userId, int groupId) {
        Teacher teacher = entityManager.find(clazz, userId);
        teacher.getGroups().removeIf(g -> g.getId() == groupId);
    }

    @Override
    public List<Teacher> findTeachersRelateToGroup(int groupId) {
        Query query = entityManager.createQuery(FIND_TEACHERS_RELATE_TO_GROUP_QUERY);
        query.setParameter("groupId", groupId);

        return query.getResultList();
    }

    @Override
    public List<Teacher> findTeachersRelateToCourse(int courseId) {
        Query query = entityManager.createQuery(FIND_TEACHERS_RELATE_TO_COURSE_QUERY);
        query.setParameter("courseId", courseId);

        return query.getResultList();
    }

    @Override
    public List<Teacher> findTeachersRelateToDepartment(int departmentId) {
        Query query = entityManager.createQuery(FIND_TEACHERS_RELATE_TO_DEPARTMENT_QUERY);
        query.setParameter("departmentId", departmentId);

        return query.getResultList();
    }

    @Override
    public void unbindTeachersFromCourse(int courseId) {
        Course course = entityManager.find(Course.class, courseId);
        course.setTeachers(new ArrayList<>());
    }

    @Override
    public void unbindTeachersFromDepartment(int departmentId) {
        List<Teacher> teachers = findTeachersRelateToDepartment(departmentId);
        teachers.forEach(t -> t.setDepartment(null));
    }

    @Override
    public void unbindTeachersFromGroup(int groupId) {
        Group group = entityManager.find(Group.class, groupId);
        group.setTeachers(new ArrayList<>());
    }

    @Override
    public void unbindTeachersFromTeacherTitle(int teacherTitleId) {
        Query query = entityManager.createQuery(FIND_TEACHERS_RELATE_TO_TEACHER_TITLE_QUERY);
        query.setParameter("teacherTitleId", teacherTitleId);
        List<Teacher> teachersRelateToTeacherTitle = query.getResultList();
        teachersRelateToTeacherTitle.forEach(t -> t.setTeacherTitle(null));
    }

    @Override
    public void addUserToGroup(int userId, int groupId) {
        Teacher teacher = entityManager.find(clazz, userId);
        Group group = entityManager.find(Group.class, groupId);
        teacher.getGroups().add(group);
    }

}
