package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CrudPageableDao;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E>
        implements CrudPageableDao<E> {

    protected final String orderByQuery;

    protected AbstractPageableCrudDaoImpl(EntityManager entityManager, Class<E> clazz,
                                          String uniqueNameParameter, String orderByQuery) {
        super(entityManager, clazz, uniqueNameParameter);
        this.orderByQuery = orderByQuery;
    }

    @Override
    public List<E> findAll(int page, int itemsPerPage) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        Root<E> root = query.from(clazz);
        CriteriaQuery<E> select = query.select(root);
        select.orderBy(criteriaBuilder.asc(root.get(orderByQuery)));

        TypedQuery<E> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult((page - 1) * itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);

        return typedQuery.getResultList();
    }

    @Override
    public long count() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(query.from(clazz)));

        return entityManager.createQuery(query).getSingleResult();
    }

}
