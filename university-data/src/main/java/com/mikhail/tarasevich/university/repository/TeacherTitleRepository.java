package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.TeacherTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherTitleRepository extends JpaRepository<TeacherTitle, Integer> {
    Optional<TeacherTitle> findByName(String name);
}
