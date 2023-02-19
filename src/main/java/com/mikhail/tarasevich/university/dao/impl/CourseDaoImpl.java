package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CourseDao;
import com.mikhail.tarasevich.university.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CourseDaoImpl extends AbstractPageableCrudDaoImpl<Course> implements CourseDao {

    private static final String SAVE_QUERY =
            "INSERT INTO courses (name, description) VALUES(?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT id, name, description FROM courses ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT id, name, description FROM courses WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY =
            "SELECT id, name, description FROM courses WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY =
            "SELECT id, name, description FROM courses ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY =
            "UPDATE courses SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY =
            "DELETE FROM courses WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM courses";
    private static final String FIND_COURSES_RELATE_TO_DEPARTMENT_QUERY =
            "SELECT id, name, description " +
                    "FROM courses " +
                    "JOIN department_courses ON courses.id = course_id " +
                    "WHERE department_id = ? " +
                    "ORDER BY courses.id";
    private static final String FIND_COURSES_RELATE_TO_TEACHER_QUERY =
            "SELECT id, name, description " +
                    "FROM courses " +
                    "JOIN user_courses ON courses.id = course_id " +
                    "WHERE user_id = ? " +
                    "ORDER BY courses.id";
    private static final String UNBIND_COURSES_FROM_TEACHER_QUERY =
            "DELETE FROM user_courses WHERE user_id = ?";
    private static final String UNBIND_COURSES_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_courses WHERE department_id = ?";
    private static final RowMapper<Course> ROW_MAPPER = (resultSet, rowNum) ->
            Course.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .withDescription(resultSet.getString("description"))
                    .build();

    @Autowired
    public CourseDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public List<Course> findCoursesRelateToDepartment(int departmentId) {
        return jdbcTemplate.query(FIND_COURSES_RELATE_TO_DEPARTMENT_QUERY, ROW_MAPPER, departmentId);
    }

    @Override
    public List<Course> findCoursesRelateToTeacher(int teacherId) {
        return jdbcTemplate.query(FIND_COURSES_RELATE_TO_TEACHER_QUERY, ROW_MAPPER, teacherId);
    }

    @Override
    public void unbindCoursesFromTeacher(int teacherId) {
        jdbcTemplate.update(UNBIND_COURSES_FROM_TEACHER_QUERY, teacherId);
    }

    @Override
    public void unbindCoursesFromDepartment(int departmentId) {
        jdbcTemplate.update(UNBIND_COURSES_FROM_DEPARTMENT_QUERY, departmentId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Course entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setString(2, entity.getDescription());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Course entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(3, entity.getId());
    }

    @Override
    protected Course makeEntityWithId(Course entity, int id) {
        return Course.builder()
                .withId(id)
                .withName(entity.getName())
                .withDescription(entity.getDescription())
                .build();
    }

}
