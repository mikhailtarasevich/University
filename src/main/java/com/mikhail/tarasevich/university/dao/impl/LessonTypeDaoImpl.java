package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.LessonTypeDao;
import com.mikhail.tarasevich.university.entity.LessonType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;

@Repository
public class LessonTypeDaoImpl extends AbstractPageableCrudDaoImpl<LessonType> implements LessonTypeDao {

    private static final String SAVE_QUERY = "INSERT INTO lesson_types (name, duration) VALUES(?, ?)";
    private static final String FIND_ALL_QUERY = "SELECT * FROM lesson_types ORDER BY id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM lesson_types WHERE id = ?";
    private static final String FIND_ALL_PAGEABLE_QUERY = "SELECT * FROM lesson_types ORDER BY id LIMIT ? OFFSET ?";
    private static final String UPDATE_QUERY = "UPDATE lesson_types SET name = ?, duration = ? WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM lesson_types WHERE id = ?";
    private static final String COUNT_TABLE_ROWS_QUERY = "SELECT COUNT(*) FROM lesson_types";
    private static final String CHANGE_DURATION_QUERY = "UPDATE lesson_types SET duration = ? WHERE id = ?";
    private static final RowMapper<LessonType> ROW_MAPPER = (resultSet, rowNum) ->
            LessonType.builder()
                    .withId(resultSet.getInt("id"))
                    .withName(resultSet.getString("name"))
                    .withDuration(Duration.ofMinutes(resultSet.getLong("duration")))
                    .build();

    @Autowired
    public LessonTypeDaoImpl(JdbcOperations jdbcTemplate) {
        super(jdbcTemplate, ROW_MAPPER, SAVE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY,
                FIND_ALL_PAGEABLE_QUERY, UPDATE_QUERY, DELETE_BY_ID_QUERY, COUNT_TABLE_ROWS_QUERY);
    }

    @Override
    public void changeDuration(int id, Duration newDuration) {
        jdbcTemplate.update(CHANGE_DURATION_QUERY, newDuration.toMinutes(), id);
    }

    @Override
    protected void setStatementForSave(PreparedStatement ps, LessonType entity) throws SQLException {
        ps.setString(1, entity.getName());
        ps.setLong(2, entity.getDuration().toMinutes());
    }

    @Override
    protected void setStatementForUpdate(PreparedStatement ps, LessonType entity) throws SQLException {
        setStatementForSave(ps, entity);
        ps.setInt(3, entity.getId());
    }

    @Override
    protected LessonType makeEntityWithId(LessonType entity, int id) {
        return LessonType.builder()
                .withId(id)
                .withName(entity.getName())
                .withDuration(entity.getDuration())
                .build();
    }

}
