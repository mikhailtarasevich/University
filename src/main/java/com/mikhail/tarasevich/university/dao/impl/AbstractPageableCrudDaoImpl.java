package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CrudPageableDao;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E> implements CrudPageableDao<E> {

    private final String findAllPageableQuery;
    private final String countTableRowsQuery;

    protected AbstractPageableCrudDaoImpl(JdbcOperations jdbcTemplate, RowMapper<E> mapper,
                                       String saveQuery, String findByIdQuery,
                                       String findAllQuery, String findAllPageableQuery, String updateQuery,
                                       String deleteByIdQuery, String countTableRowsQuery) {
        super(jdbcTemplate, mapper, saveQuery, findByIdQuery, findAllQuery, updateQuery, deleteByIdQuery);
        this.findAllPageableQuery = findAllPageableQuery;
        this.countTableRowsQuery = countTableRowsQuery;
    }

    @Override
    public List<E> findAll(int page, int itemsPerPage) {
        int offsetToPage = page * itemsPerPage;
        return jdbcTemplate.query(findAllPageableQuery, new Object[]{itemsPerPage, offsetToPage}, mapper);
    }

    @Override
    public long count() {
        return jdbcTemplate.queryForObject(countTableRowsQuery, Long.class);
    }

}
