package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.TeacherTitleDao;
import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class TeacherTitleDaoImpl extends AbstractPageableCrudDaoImpl<TeacherTitle> implements TeacherTitleDao {

    private static final String SAVE_QUERY = "INSERT INTO teacher_titles (name) VALUES(?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM teacher_titles ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM teacher_titles WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM teacher_titles WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM teacher_titles ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE teacher_titles SET name = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM teacher_titles WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM teacher_titles";
    private static final RowMapper<TeacherTitle> ROW_MAPPER = (resultSet, rowNum) ->
            TeacherTitle.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .build();

    @Autowired
    public TeacherTitleDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, TeacherTitle entity) throws SQLException {
        ps.setString(1, entity.getName());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, TeacherTitle entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getId());
    }

    @Override
    protected TeacherTitle makeEntityWithId(TeacherTitle teacherTitle, int id) {
        return TeacherTitle.builder()
                .withId(id)
                .withName(teacherTitle.getName())
                .build();
    }

}
