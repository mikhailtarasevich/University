package com.mikhail.tarasevich.university.dao.impl;

import com.mikhail.tarasevich.university.dao.CrudDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Transactional
public abstract class AbstractCrudDaoImpl<E> implements CrudDao<E> {

    protected final EntityManager entityManager;
    protected final Class<E> clazz;
    protected final String uniqueNameParameter;

    protected AbstractCrudDaoImpl(EntityManager entityManager, Class<E> clazz, String uniqueNameParameter) {
        this.entityManager = entityManager;
        this.clazz = clazz;
        this.uniqueNameParameter = uniqueNameParameter;
    }

    @Override
    public E save(E entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void saveAll(List<E> entities) {
        entities.forEach(entityManager::merge);
    }

    @Override
    public Optional<E> findById(int id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        Root<E> root = query.from(clazz);
        query.select(root).orderBy(criteriaBuilder.asc(root.get("id")));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Optional<E> findByName(String name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = criteriaBuilder.createQuery(clazz);
        Root<E> root = query.from(clazz);
        Predicate namePredicate = criteriaBuilder.equal(root.get(uniqueNameParameter), name);
        query.select(root).where(namePredicate);

        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }


    @Override
    public void updateAll(List<E> entities) {
        entities.forEach(entityManager::merge);
    }

    @Override
    public boolean deleteById(int id) {
        E entity = entityManager.find(clazz, id);
        if (entity != null) {
            entityManager.remove(entity);
            return true;
        } else {
            log.info("Entity with id = {} wasn't deleted from DB, cause it wasn't found in DB.", id);
            return false;
        }
    }

    @Override
    public boolean deleteByIds(Set<Integer> ids) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<E> criteriaDelete = criteriaBuilder.createCriteriaDelete(clazz);
        Root<E> root = criteriaDelete.from(clazz);
        criteriaDelete.where(root.get("id").in(ids));
        int count = entityManager.createQuery(criteriaDelete).executeUpdate();

        if (count != ids.size()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info("Entities with ids = {} wasn't deleted from DB. Transaction was rolled back.", ids);
            return false;
        }

        return true;
    }

}
