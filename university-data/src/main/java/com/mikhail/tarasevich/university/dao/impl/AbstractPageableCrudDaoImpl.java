package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CrudPageableDao;
import com.mikhail.tarasevich.university.entity.Lesson;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractPageableCrudDaoImpl<E> extends AbstractCrudDaoImpl<E>
        implements CrudPageableDao<E> {

    protected AbstractPageableCrudDaoImpl(SessionFactory sessionFactory, Class<E> clazz,
                                          String uniqueNameParameter) {
        super(sessionFactory, clazz, uniqueNameParameter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll(int page, int itemsPerPage) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        Root<E> root = query.from(clazz);
        CriteriaQuery<E> select = query.select(root);
        if (clazz.equals(Lesson.class)) {
            select.orderBy(criteriaBuilder.asc(root.get("startTime")));
        } else {
            select.orderBy(criteriaBuilder.asc(root.get("id")));
        }

        TypedQuery<E> typedQuery = session.createQuery(select);
        typedQuery.setFirstResult((page-1)*itemsPerPage);
        typedQuery.setMaxResults(itemsPerPage);
        return typedQuery.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        query.select(criteriaBuilder.count(query.from(clazz)));

        return session.createQuery(query).getSingleResult();
    }

}
