package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.UserDao;
import com.mikhail.tarasevich.university.entity.User;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;


public abstract class AbstractUserDaoImpl<E extends User> extends AbstractPageableCrudDaoImpl<E> implements UserDao<E> {

    protected final String updateGeneralUserInfoQuery;
    protected final String updatePasswordQuery;
    protected final String addUserToGroupQuery;

    protected AbstractUserDaoImpl(JdbcOperations jdbcTemplate, RowMapper<E> mapper,
                                  String saveQuery, String findByIdQuery, String findAllQuery, String findByNameQuery,
                                  String findAllPageableQuery, String updateQuery, String deleteByIdQuery,
                                  String countTableRowsQuery, String updateGeneralUserInfoQuery,
                                  String updatePasswordQuery, String addUserToGroupQuery) {
        super(jdbcTemplate, mapper, saveQuery, findByIdQuery, findAllQuery, findByNameQuery, findAllPageableQuery,
                updateQuery, deleteByIdQuery, countTableRowsQuery);
        this.updateGeneralUserInfoQuery = updateGeneralUserInfoQuery;
        this.updatePasswordQuery = updatePasswordQuery;
        this.addUserToGroupQuery = addUserToGroupQuery;
    }

    @Override
    public void updateGeneralUserInfo(E entity) {
        jdbcTemplate.update(updateGeneralUserInfoQuery, entity.getFirstName(), entity.getLastName(),
                entity.getGender().ordinal(), entity.getEmail(), entity.getId());
    }

    @Override
    public void updateUserPassword(int id, String password) {
        jdbcTemplate.update(updatePasswordQuery, password, id);
    }

    @Override
    public void addUserToGroup(int userId, int groupId) {
        jdbcTemplate.update(addUserToGroupQuery, groupId, userId);
    }

}
