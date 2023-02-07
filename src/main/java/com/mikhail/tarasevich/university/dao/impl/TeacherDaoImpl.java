package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherDao;
import com.mikhail.tarasevich.university.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TeacherDaoImpl extends AbstractUserDaoImpl<Teacher> implements TeacherDao {

    private static final String SAVE_QUERY =
            "INSERT INTO users (user_type, first_name, last_name, gender, email, password, " +
                    "teacher_title_id, department_id) VALUES('teacher', ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_COMMON_PART_QUERY =
            "SELECT users.id AS user_id, first_name, last_name, gender, email, password, " +
                    "teacher_title_id, department_id FROM users ";
    private static final String FIND_ALL_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'teacher' ORDER BY users.id";
    private static final String FIND_BY_ID_QUERY = FIND_COMMON_PART_QUERY + "WHERE user_type = 'teacher' AND id = ?";
    private static final String FIND_BY_EMAIL_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'teacher' AND email = ? ORDER BY users.id";
    private static final String FIND_ALL_PAGEABLE_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'teacher' ORDER BY users.id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE users SET first_name = ?, last_name = ?, gender = ?, email = ?, password = ?, " +
                    "teacher_title_id = ?, department_id = ? WHERE user_type = 'teacher' AND users.id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE user_type = 'teacher' AND id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM users WHERE user_type = 'teacher'";
    private static final String ADD_TEACHER_TO_GROUP_QUERY = "INSERT INTO user_groups (group_id, user_id) VALUES(?, ?)";
    private static final String DELETE_TEACHER_FROM_GROUP =
            "DELETE FROM user_groups WHERE user_id = ? AND group_id = ?";
    private static final String ADD_TEACHER_TO_COURSE_QUERY =
            "INSERT INTO user_courses (user_id, course_id) VALUES(?, ?)";
    private static final String DELETE_TEACHER_FROM_COURSE =
            "DELETE FROM user_courses WHERE user_id = ? AND course_id = ?";
    private static final String CHANGE_TEACHER_TITLE_QUERY =
            "UPDATE users SET teacher_title_id = ? WHERE user_type = 'teacher' AND id = ?";
    private static final String CHANGE_DEPARTMENT_QUERY =
            "UPDATE users SET department_id = ? WHERE user_type = 'teacher' AND id = ?";
    private static final String FIND_TEACHERS_RELATE_TO_GROUP_QUERY =
            "SELECT users.id AS user_id, first_name, last_name, gender, email, password, " +
                    "teacher_title_id, department_id, user_groups.group_id " +
                    "FROM users " +
                    "JOIN user_groups ON users.id = user_groups.user_id " +
                    "JOIN groups ON user_groups.group_id = groups.id " +
                    "WHERE user_type = 'teacher' AND groups.id = ? " +
                    "ORDER BY users.id";
    private static final String FIND_TEACHERS_RELATE_TO_COURSE_QUERY =
            "SELECT users.id AS user_id, first_name, last_name, gender, email, password, " +
                    "teacher_title_id, department_id, user_courses.course_id " +
                    "FROM users " +
                    "JOIN user_courses ON users.id = user_courses.user_id " +
                    "JOIN courses ON user_courses.course_id = courses.id " +
                    "WHERE user_type = 'teacher' AND courses.id = ? " +
                    "ORDER BY users.id";
    private static final String FIND_TEACHERS_RELATE_TO_DEPARTMENT_QUERY =
            "SELECT users.id AS user_id, first_name, last_name, gender, email, password, " +
                    "teacher_title_id, department_id " +
                    "FROM users WHERE user_type = 'teacher' AND department_id = ? ORDER BY id";
    private static final String UNBIND_TEACHERS_FROM_COURSE_QUERY =
            "DELETE FROM user_courses WHERE course_id = ?";
    private static final String UNBIND_TEACHERS_FROM_DEPARTMENT_QUERY =
            "UPDATE users SET department_id = NULL WHERE user_type = 'teacher' AND department_id = ?";
    private static final String UNBIND_TEACHERS_FROM_GROUP_QUERY =
            "DELETE FROM user_groups WHERE group_id = ?";
    private static final String UNBIND_TEACHERS_FROM_TEACHER_TITLE_QUERY =
            "UPDATE users SET teacher_title_id = NULL WHERE user_type = 'teacher' AND teacher_title_id = ?";
    private static final RowMapper<Teacher> ROW_MAPPER = (resultSet, rowNum) ->
            Teacher.builder()
                    .withId(resultSet.getInt("user_id"))
                    .withFirstName(resultSet.getString("first_name"))
                    .withLastName(resultSet.getString("last_name"))
                    .withGender(Gender.getById(resultSet.getInt("gender")))
                    .withEmail(resultSet.getString("email"))
                    .withPassword(resultSet.getString("password"))
                    .withTeacherTitle(
                            TeacherTitle.builder().withId(resultSet.getInt("teacher_title_id")).build()
                    )
                    .withDepartment(Department.builder().withId(resultSet.getInt("department_id")).build())
                    .build();

    @Autowired
    public TeacherDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_EMAIL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY,
                ADD_TEACHER_TO_GROUP_QUERY);
    }

    @Override
    public void addTeacherToCourse(int teacherId, Integer courseId) {
        jdbcTemplate.update(ADD_TEACHER_TO_COURSE_QUERY, teacherId, courseId);
    }

    @Override
    public void deleteTeacherFromCourse(int teacherId, int courseId) {
        jdbcTemplate.update(DELETE_TEACHER_FROM_COURSE, teacherId, courseId);
    }

    @Override
    public void changeDepartment(int teacherId, Integer newDepartmentId) {
        jdbcTemplate.update(CHANGE_DEPARTMENT_QUERY, newDepartmentId, teacherId);
    }

    @Override
    public void changeTeacherTitle(int teacherId, Integer newTeacherTitleId) {
        jdbcTemplate.update(CHANGE_TEACHER_TITLE_QUERY, newTeacherTitleId, teacherId);
    }

    @Override
    public void deleteTeacherFromGroup(int userId, int groupId) {
        jdbcTemplate.update(DELETE_TEACHER_FROM_GROUP, userId, groupId);
    }

    @Override
    public List<Teacher> findTeachersRelateToGroup(int groupId) {
        return jdbcTemplate.query(FIND_TEACHERS_RELATE_TO_GROUP_QUERY, ROW_MAPPER, groupId);
    }

    @Override
    public List<Teacher> findTeachersRelateToCourse(int courseId) {
        return jdbcTemplate.query(FIND_TEACHERS_RELATE_TO_COURSE_QUERY, ROW_MAPPER, courseId);
    }

    @Override
    public List<Teacher> findTeachersRelateToDepartment(int departmentId) {
        return jdbcTemplate.query(FIND_TEACHERS_RELATE_TO_DEPARTMENT_QUERY, ROW_MAPPER, departmentId);
    }

    @Override
    public void unbindTeachersFromCourse(int courseId) {
        jdbcTemplate.update(UNBIND_TEACHERS_FROM_COURSE_QUERY, courseId);
    }

    @Override
    public void unbindTeachersFromDepartment(int departmentId) {
        jdbcTemplate.update(UNBIND_TEACHERS_FROM_DEPARTMENT_QUERY, departmentId);
    }

    @Override
    public void unbindTeachersFromGroup(int groupId) {
        jdbcTemplate.update(UNBIND_TEACHERS_FROM_GROUP_QUERY, groupId);
    }


    @Override
    public void unbindTeachersFromTeacherTitle(int teacherTitleId) {
        jdbcTemplate.update(UNBIND_TEACHERS_FROM_TEACHER_TITLE_QUERY, teacherTitleId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Teacher entity) throws SQLException {
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
        ps.setInt(3, entity.getGender().ordinal());
        ps.setString(4, entity.getEmail());
        ps.setString(5, entity.getPassword());
        ps.setInt(6, entity.getTeacherTitle().getId());
        ps.setInt(7, entity.getDepartment().getId());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Teacher entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(8, entity.getId());
    }

    @Override
    protected Teacher makeEntityWithId(Teacher entity, int id) {
        return Teacher.builder()
                .withId(id)
                .withFirstName(entity.getFirstName())
                .withLastName(entity.getLastName())
                .withGender(entity.getGender())
                .withEmail(entity.getEmail())
                .withPassword(entity.getPassword())
                .withTeacherTitle(entity.getTeacherTitle())
                .withDepartment(entity.getDepartment())
                .build();
    }

}
