package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.GroupDao;
import com.mikhail.tarasevich.university.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GroupDaoImpl extends AbstractPageableCrudDaoImpl<Group> implements GroupDao {

    private static final String SAVE_QUERY =
            "INSERT INTO groups (name, faculty_id, head_user_id, education_form_id) VALUES(?, ?, ?, ?)";
    private static final String FIND_COMMON_PART_QUERY =
            "SELECT groups.id AS group_id, groups.name AS group_name, faculties.id AS faculty_id, " +
                    "faculties.name AS faculty_name, faculties.description AS faculty_description, " +
                    "users.id AS student_id, first_name, last_name, gender, email, password, " +
                    "users.group_id AS user_group_id, education_forms.id AS education_form_id, " +
                    "education_forms.name AS education_form_name " +
                    "FROM groups " +
                    "LEFT JOIN faculties ON faculty_id = faculties.id " +
                    "LEFT JOIN users ON groups.head_user_id = users.id " +
                    "LEFT JOIN education_forms ON education_form_id = education_forms.id ";
    private static final String FIND_ALL_QUERY = FIND_COMMON_PART_QUERY + "ORDER BY groups.id";
    private static final String FIND_BY_ID_QUERY = FIND_COMMON_PART_QUERY + "WHERE groups.id = ?";
    private static final String FIND_BY_NAME_QUERY =
            FIND_COMMON_PART_QUERY + "WHERE groups.name = ? ORDER BY groups.id";
    private static final String FIND_ALL_PAGEABLE_QUERY = FIND_COMMON_PART_QUERY +
            "ORDER BY groups.id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE groups SET name = ?, faculty_id = ?, head_user_id = ?, education_form_id = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM groups WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM groups";
    private static final String UPDATE_FACULTY_QUERY = "UPDATE groups SET faculty_id = ? WHERE id = ?";
    private static final String UPDATE_HEAD_USER_QUERY = "UPDATE groups SET head_user_id = ? WHERE id = ?";
    private static final String UPDATE_EDUCATION_FORM_QUERY = "UPDATE groups SET education_form_id = ? WHERE id = ?";
    private static final String FIND_GROUPS_RELATE_TO_TEACHER_QUERY = FIND_COMMON_PART_QUERY +
            "LEFT JOIN user_groups ON groups.id = user_groups.group_id WHERE user_id = ? ORDER BY groups.id";
    private static final String UNBIND_GROUPS_FROM_STUDENT_QUERY =
            "UPDATE groups SET head_user_id = NULL WHERE head_user_id = ?";
    private static final String UNBIND_GROUPS_FROM_TEACHER_QUERY =
            "DELETE FROM user_groups WHERE user_id = ?";
    private static final String UNBIND_GROUPS_FROM_EDUCATION_FORM_QUERY =
            "UPDATE groups SET education_form_id = NULL WHERE education_form_id = ?";
    private static final String UNBIND_GROUPS_FROM_FACULTY_QUERY =
            "UPDATE groups SET faculty_id = NULL WHERE faculty_id = ?";
    private static final RowMapper<Group> ROW_MAPPER = (resultSet, rowNum) ->
            Group.builder()
                    .withId(resultSet.getInt("group_id"))
                    .withName(resultSet.getString("group_name"))
                    .withFaculty(
                            Faculty.builder()
                                    .withId(resultSet.getInt("faculty_id"))
                                    .withName(resultSet.getString("faculty_name"))
                                    .withDescription(resultSet.getString("faculty_description"))
                                    .build())
                    .withHeadStudent(
                            Student.builder()
                                    .withId(resultSet.getInt("student_id"))
                                    .withFirstName(resultSet.getString("first_name"))
                                    .withLastName(resultSet.getString("last_name"))
                                    .withGender(Gender.getById(resultSet.getInt("gender")))
                                    .withEmail(resultSet.getString("email"))
                                    .withPassword(resultSet.getString("password"))
                                    .withGroup(Group.builder()
                                            .withId(resultSet.getInt("user_group_id"))
                                            .build())
                                    .build())
                    .withEducationForm(
                            EducationForm.builder()
                                    .withId(resultSet.getInt("education_form_id"))
                                    .withName(resultSet.getString("education_form_name"))
                                    .build())
                    .build();

    @Autowired
    public GroupDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public void changeFaculty(int groupId, int newFacultyId) {
        jdbcTemplate.update(UPDATE_FACULTY_QUERY, newFacultyId, groupId);
    }

    @Override
    public void changeHeadUser(int groupId, int newUserId) {
        jdbcTemplate.update(UPDATE_HEAD_USER_QUERY, newUserId, groupId);
    }

    @Override
    public void changeEducationForm(int groupId, int newEducationFormId) {
        jdbcTemplate.update(UPDATE_EDUCATION_FORM_QUERY, newEducationFormId, groupId);
    }

    @Override
    public List<Group> findGroupsRelateToTeacher(int teacherId) {
        return jdbcTemplate.query(FIND_GROUPS_RELATE_TO_TEACHER_QUERY, ROW_MAPPER, teacherId);
    }

    @Override
    public void unbindGroupsFromStudent(int studentId) {
        jdbcTemplate.update(UNBIND_GROUPS_FROM_STUDENT_QUERY, studentId);
    }

    @Override
    public void unbindGroupsFromTeacher(int teacherId) {
        jdbcTemplate.update(UNBIND_GROUPS_FROM_TEACHER_QUERY, teacherId);
    }

    @Override
    public void unbindGroupsFromEducationForm(int educationFormId) {
        jdbcTemplate.update(UNBIND_GROUPS_FROM_EDUCATION_FORM_QUERY, educationFormId);
    }

    @Override
    public void unbindGroupsFromFaculty(int facultyId) {
        jdbcTemplate.update(UNBIND_GROUPS_FROM_FACULTY_QUERY, facultyId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Group entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getFaculty().getId());
        ps.setInt(3, entity.getHeadStudent().getId());
        ps.setInt(4, entity.getEducationForm().getId());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Group entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(5, entity.getId());
    }

    @Override
    protected Group makeEntityWithId(Group entity, int id) {
        return Group.builder()
                .withId(id)
                .withName(entity.getName())
                .withFaculty(entity.getFaculty())
                .withHeadStudent(entity.getHeadStudent())
                .withEducationForm(entity.getEducationForm())
                .build();
    }

}
