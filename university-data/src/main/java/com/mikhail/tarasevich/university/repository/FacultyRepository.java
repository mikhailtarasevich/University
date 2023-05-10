package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {

    Optional<Faculty> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Faculty f SET f.name = :name, f.description = :description WHERE f.id = :id")
    void update(@Param("id") int id, @Param("name") String name, @Param("description") String description);

}
