package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.FacultyDao;
import com.mikhail.tarasevich.university.entity.Faculty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class FacultyDaoImpl extends AbstractPageableCrudDaoImpl<Faculty> implements FacultyDao {

    private static final String SAVE_QUERY = "INSERT INTO faculties (name, description) VALUES(?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM faculties ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM faculties WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM faculties WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM faculties ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE faculties SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM faculties WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM faculties";
    private static final RowMapper<Faculty> ROW_MAPPER = (resultSet, rowNum) ->
            Faculty.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .withDescription(resultSet.getString("description"))
                    .build();

    @Autowired
    public FacultyDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, Faculty entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setString(2, entity.getDescription());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, Faculty entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(3, entity.getId());
    }

    @Override
    protected Faculty makeEntityWithId(Faculty faculty, int id) {
        return Faculty.builder()
                .withId(id)
                .withName(faculty.getName())
                .withDescription(faculty.getDescription())
                .build();
    }

}
