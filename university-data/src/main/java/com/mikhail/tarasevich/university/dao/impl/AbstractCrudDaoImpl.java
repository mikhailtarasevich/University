package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CrudDao;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.criteria.*;
import java.util.*;

@Transactional
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractCrudDaoImpl.class);

    protected final SessionFactory sessionFactory;
    protected final Class<E> clazz;
    protected final String uniqueNameParameter;

    public AbstractCrudDaoImpl(SessionFactory sessionFactory, Class<E> clazz, String uniqueNameParameter) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
        this.uniqueNameParameter = uniqueNameParameter;
    }

    @Override
    public E save(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.save(entity);

        return entity;
    }

    @Override
    public void saveAll(List<E> entities) {
        Session session = sessionFactory.getCurrentSession();
        entities.forEach(session::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<E> findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.of(session.get(clazz, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        Root<E> root = query.from(clazz);
        query.select(root).orderBy(criteriaBuilder.asc(root.get("id")));

        return session.createQuery(query).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<E> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        Root<E> root = query.from(clazz);
        Predicate emailPredicate = criteriaBuilder.equal(root.get(uniqueNameParameter), name);
        query.select(root).where(emailPredicate);
        session.createQuery(query).uniqueResultOptional();

        return session.createQuery(query).uniqueResultOptional();
    }

    @Override
    public void update(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.update(entity);
    }


    @Override
    public void updateAll(List<E> entities) {
        Session session = sessionFactory.getCurrentSession();
        entities.forEach(session::update);
    }

    @Override
    public boolean deleteById(int id) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.delete(session.get(clazz, id));
            return true;
        } catch (HibernateException e) {
            LOG.info("Entity with id = {} wasn't deleted from DB.", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        boolean status = true;

        CriteriaDelete<E> criteria = criteriaBuilder.createCriteriaDelete(clazz);
        Root<E> root = criteria.from(clazz);
        criteria.where(root.get("id").in(ids));
        int count = session.createQuery(criteria).executeUpdate();
        if (count != ids.size()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            status = false;
            LOG.info("Entities with ids = {} wasn't deleted from DB. Transaction was rolled back.", ids);
        }

        return status;
    }

}
