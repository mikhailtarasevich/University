package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.EducationFormDao;
import com.mikhail.tarasevich.university.entity.EducationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class EducationFormDaoImpl extends AbstractPageableCrudDaoImpl<EducationForm> implements EducationFormDao {

    private static final String SAVE_QUERY = "INSERT INTO education_forms (name) VALUES(?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM education_forms ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM education_forms WHERE id = ?";
    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM education_forms WHERE name = ? ORDER BY id";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM education_forms ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE education_forms SET name = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM education_forms WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM education_forms";
    private static final RowMapper<EducationForm> ROW_MAPPER = (resultSet, rowNumber) ->
            EducationForm.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .build();

    @Autowired
    public EducationFormDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, FIND_ALL_PAGEABLE_QUERY,
                FIND_BY_NAME_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, EducationForm entity) throws SQLException {
        ps.setString(1, entity.getName());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, EducationForm entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setInt(2, entity.getId());
    }

    @Override
    protected EducationForm makeEntityWithId(EducationForm entity, int id) {
        return EducationForm.builder()
                .withId(id)
                .withName(entity.getName())
                .build();
    }

}
