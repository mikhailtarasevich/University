package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.StudentDao;
import com.mikhail.tarasevich.university.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
public class StudentDaoImpl extends AbstractUserDaoImpl<Student> implements StudentDao {

    private static final String SAVE_QUERY = "INSERT INTO users (user_type, first_name, last_name, gender, email, " +
            "password, group_id) VALUES('student', ?, ?, ?, ?, ?, ?)";
    private static final String FIND_COMMON_PART_QUERY =
            "SELECT users.id AS user_id, first_name, last_name, gender, email, password, " +
                    "group_id, groups.name AS group_name, faculty_id, head_user_id, education_form_id " +
                    "FROM users " +
                    "LEFT JOIN groups ON group_id = groups.id ";
    private static final String FIND_ALL_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'student' ORDER BY users.id";
    private static final String FIND_BY_ID_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'student' AND users.id = ?";
    private static final String FIND_BY_EMAIL_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'student' AND email = ? ORDER BY users.id";
    private static final String FIND_ALL_PAGEABLE_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE user_type = 'student' ORDER BY users.id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name = ?, last_name = ?, gender = ?, " +
            "email = ?, password = ?, group_id = ? WHERE users.id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM users WHERE user_type = 'student'";
    private static final String ADD_STUDENT_TO_GROUP_QUERY = "UPDATE users SET group_id = ? WHERE id = ?";
    private static final String DELETE_STUDENT_FROM_GROUP_QUERY = "UPDATE users SET group_id = null WHERE id = ?";
    private static final String FIND_STUDENTS_RELATE_TO_GROUP_QUERY =
            "SELECT users.id AS user_id, first_name, last_name, gender, email, password, " +
                    "group_id, groups.name AS group_name, faculty_id, head_user_id, education_form_id " +
                    "FROM users " +
                    "JOIN groups ON group_id = groups.id " +
                    "WHERE user_type = 'student' AND group_id = ? " +
                    "ORDER BY user_id";
    private static final String UPDATE_GENERAL_STUDENT_INFO_QUERY =
            "UPDATE users SET first_name = ?, last_name = ?, gender = ?, email = ? WHERE id = ?";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE users SET password = ? WHERE id = ?";
    private static final String UNBIND_STUDENTS_FROM_GROUP_QUERY =
            "UPDATE users SET group_id = NULL WHERE group_id = ?";
    private static final RowMapper<Student> ROW_MAPPER = (resultSet, rowNum) ->
            Student.builder()
                    .withId(resultSet.getInt("user_id"))
                    .withFirstName(resultSet.getString("first_name"))
                    .withLastName(resultSet.getString("last_name"))
                    .withGender(Gender.getById(resultSet.getInt("gender")))
                    .withEmail(resultSet.getString("email"))
                    .withPassword(resultSet.getString("password"))
                    .withGroup(Group.builder()
                            .withId(resultSet.getInt("group_id"))
                            .withName(resultSet.getString("group_name"))
                            .withFaculty(Faculty.builder().withId(resultSet.getInt("faculty_id")).build())
                            .withHeadStudent(
                                    Student.builder().withId(resultSet.getInt("head_user_id")).build()
                            )
                            .withEducationForm(
                                    EducationForm.builder().withId(resultSet.getInt("education_form_id"))
                                            .build()
                            )
                            .build())
                    .build();

    @Autowired
    public StudentDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_EMAIL_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY,
                UPDATE_GENERAL_STUDENT_INFO_QUERY, UPDATE_PASSWORD_QUERY, ADD_STUDENT_TO_GROUP_QUERY);
    }

    @Override
    public void deleteStudentFromGroup(int studentId) {
        jdbcTemplate.update(DELETE_STUDENT_FROM_GROUP_QUERY, studentId);
    }

    @Override
    public List<Student> findStudentsRelateToGroup(int groupId) {
        return jdbcTemplate.query(FIND_STUDENTS_RELATE_TO_GROUP_QUERY, ROW_MAPPER, groupId);
    }

    @Override
    public void unbindStudentsFromGroup(int groupId) {
        jdbcTemplate.update(UNBIND_STUDENTS_FROM_GROUP_QUERY, groupId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Student entity) throws SQLException {
        ps.setString(1, entity.getFirstName());
        ps.setString(2, entity.getLastName());
        ps.setInt(3, entity.getGender().ordinal());
        ps.setString(4, entity.getEmail());
        ps.setString(5, entity.getPassword());
        ps.setInt(6, entity.getGroup().getId());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Student entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(7, entity.getId());
    }

    @Override
    protected Student makeEntityWithId(Student entity, int id) {
        return Student.builder()
                .withId(id)
                .withFirstName(entity.getFirstName())
                .withLastName(entity.getLastName())
                .withGender(entity.getGender())
                .withEmail(entity.getEmail())
                .withPassword(entity.getPassword())
                .withGroup(entity.getGroup())
                .build();
    }

}
