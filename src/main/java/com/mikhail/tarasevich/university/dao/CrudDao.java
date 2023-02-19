package com.mikhail.tarasevich.university.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrudDao<E> {

    //create
    E save(E entity);
    void saveAll(List<E> entity);

    //read
    Optional<E> findById(int id);
    List<E> findAll();
    Optional<E> findByName(String name);
//    List<E> findByName(String name);

    //update
    void update(E entity);
    void updateAll(List<E> entity);

    //delete
    boolean deleteById(int param);
    boolean deleteByIds(Set<Integer> ids);

}
