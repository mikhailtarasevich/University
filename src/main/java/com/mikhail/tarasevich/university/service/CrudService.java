package com.mikhail.tarasevich.university.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CrudService <REQUEST, RESPONSE>{

    RESPONSE register(REQUEST requestDTO);
    void registerAll(List<REQUEST> requestDTOs);

    RESPONSE findById(int id);
    List<RESPONSE> findAll(String page);
    List<RESPONSE> findAll();

    void edit(REQUEST requestDTO);
    void editAll(List<REQUEST> request);

    boolean deleteById(int id);
    boolean deleteByIds(Set<Integer> ids);

}
