package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.DepartmentDao;
import com.mikhail.tarasevich.university.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class DepartmentDaoImpl extends AbstractPageableCrudDaoImpl<Department> implements DepartmentDao {

    private static final String SAVE_QUERY = "INSERT INTO departments (name, description) VALUES(?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM departments ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM departments WHERE id = ? ORDER BY id";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM departments WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM departments ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE departments SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM departments WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM departments";
    private static final String ADD_COURSE_TO_DEPARTMENT_QUERY =
            "INSERT INTO department_courses (department_id, course_id) VALUES (?, ?)";
    private static final String DELETE_COURSE_FROM_DEPARTMENT_QUERY =
            "DELETE FROM department_courses WHERE department_id = ? AND course_id = ?";
    private static final String UNBIND_DEPARTMENTS_FROM_COURSE_QUERY =
            "DELETE FROM department_courses WHERE course_id = ?";
    private static final RowMapper<Department> ROW_MAPPER = (resultSet, rowNum) ->
            Department.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .withDescription(resultSet.getString("description"))
                    .build();

    @Autowired
    public DepartmentDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public void addCourseToDepartment(int departmentId, int courseId) {
        jdbcTemplate.update(ADD_COURSE_TO_DEPARTMENT_QUERY, departmentId, courseId);
    }

    @Override
    public void deleteCourseFromDepartment(int departmentId, int courseId) {
        jdbcTemplate.update(DELETE_COURSE_FROM_DEPARTMENT_QUERY, departmentId, courseId);
    }

    @Override
    public void unbindDepartmentsFromCourse(int courseId){
        jdbcTemplate.update(UNBIND_DEPARTMENTS_FROM_COURSE_QUERY, courseId);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Department entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setString(2, entity.getDescription());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Department entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(3, entity.getId());
    }

    @Override
    protected Department makeEntityWithId(Department entity, int id) {
        return Department.builder()
                .withId(id)
                .withName(entity.getName())
                .withDescription(entity.getDescription())
                .build();
    }

}
