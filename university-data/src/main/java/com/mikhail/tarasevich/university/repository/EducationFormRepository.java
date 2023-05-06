package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.EducationForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationFormRepository extends JpaRepository<EducationForm, Integer> {
    Optional<EducationForm> findByName(String name);
}
