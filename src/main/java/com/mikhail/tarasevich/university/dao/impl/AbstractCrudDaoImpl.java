package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CrudDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCrudDaoImpl.class);

    protected final JdbcOperations jdbcTemplate;
    protected RowMapper<E> mapper;
    protected static final KeyHolder keyHolder = new GeneratedKeyHolder();
    private final String saveQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String findByNameQuery;
    private final String updateQuery;
    private final String deleteByIdQuery;

    protected AbstractCrudDaoImpl(JdbcOperations jdbcTemplate, RowMapper<E> mapper,
                                  String saveQuery, String findByIdQuery, String findAllQuery, String findByNameQuery,
                                  String updateQuery, String deleteByIdQuery) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.saveQuery = saveQuery;
        this.findByIdQuery = findByIdQuery;
        this.findAllQuery = findAllQuery;
        this.findByNameQuery = findByNameQuery;
        this.updateQuery = updateQuery;
        this.deleteByIdQuery = deleteByIdQuery;
    }

    @Override
    public E save(E entity) {
        try {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(saveQuery, Statement.RETURN_GENERATED_KEYS);
                setStatementForSave(ps, entity);
                return ps;
            }, keyHolder);
            return makeEntityWithId(entity, (int) keyHolder.getKeys().get("id"));
        } catch (NullPointerException e) {
            LOG.error("KeyHolder is null, unable to save entity", e);
            return null;
        }
    }

    @Override
    public void saveAll(List<E> entities) {
        jdbcTemplate.batchUpdate(saveQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                setStatementForSave(ps, entities.get(i));
            }

            @Override
            public int getBatchSize() {
                return entities.size();
            }

        });
    }

    @Override
    public Optional<E> findById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findByIdQuery, mapper, id));
        } catch (DataAccessException e) {
            LOG.info("Entity with id = {} not found in DB. Query: {} ", id, findAllQuery);
            return Optional.empty();
        }
    }

    @Override
    public List<E> findAll() {
        return jdbcTemplate.query(findAllQuery, mapper);
    }

    @Override
    public Optional<E> findByName(String name){
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(findByNameQuery, mapper, name));
        } catch (DataAccessException e) {
            LOG.info("Entity with name = {} not found in DB. Query: {} ", name, findByNameQuery);
            return Optional.empty();
        }
    }

    @Override
    public void update(E entity) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(updateQuery);
            setStatementForUpdate(ps, entity);
            return ps;
        });
    }


    @Override
    public void updateAll(List<E> entities) {
        jdbcTemplate.batchUpdate(updateQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        setStatementForUpdate(ps, entities.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return entities.size();
                    }
                });
    }

    @Override
    public boolean deleteById(int id) {
        int result = jdbcTemplate.update(deleteByIdQuery, id);
        if (result == 1) {
            LOG.info("Entity with id = {} was deleted from DB. Query: {}", id, deleteByIdQuery);
            return true;
        } else {
            LOG.info("Entity with id = {} wasn't deleted from DB. Query: {}", id, deleteByIdQuery);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        List<Integer> listIds = new ArrayList<>(ids);
        boolean status = true;
        int[] result = jdbcTemplate.batchUpdate(deleteByIdQuery,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int numberOfElement)
                            throws SQLException {
                        int id = listIds.get(numberOfElement);
                        preparedStatement.setLong(1, id);
                    }

                    @Override
                    public int getBatchSize() {
                        return ids.size();
                    }
                });
        if (Arrays.stream(result).anyMatch(a -> a == 0)) {
            status = false;
        }

        return status;
    }

    protected abstract void setStatementForSave(PreparedStatement ps, E entity) throws SQLException;

    protected abstract void setStatementForUpdate(PreparedStatement ps, E entity) throws SQLException;

    protected abstract E makeEntityWithId(E entity, int id);

}
