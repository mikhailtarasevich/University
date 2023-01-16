package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.UserDao;
import com.mikhail.tarasevich.university.entity.User;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;


public abstract class AbstractUserDaoImpl<E extends User> extends AbstractPageableCrudDaoImpl<E> implements UserDao<E> {

    protected final String addUserToGroupQuery;

    protected AbstractUserDaoImpl(JdbcOperations jdbcTemplate, RowMapper<E> mapper,
                               String saveQuery, String findByIdQuery, String findAllQuery, String findAllPageableQuery,
                               String updateQuery, String deleteByIdQuery,
                               String countTableRowsQuery, String addUserToGroupQuery) {
        super(jdbcTemplate, mapper, saveQuery, findByIdQuery, findAllQuery, findAllPageableQuery, updateQuery,
                deleteByIdQuery, countTableRowsQuery);
        this.addUserToGroupQuery = addUserToGroupQuery;
    }

    @Override
    public void addUserToGroup(int userId, Integer groupId) {
        jdbcTemplate.update(addUserToGroupQuery, new Object[]{groupId, userId});
    }

}
