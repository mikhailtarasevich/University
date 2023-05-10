package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.LessonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Optional;

@Repository
public interface LessonTypeRepository extends JpaRepository<LessonType, Integer> {

    Optional<LessonType> findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE LessonType l SET l.duration = :newDuration WHERE l.id = :lessonTypeId")
    void changeDuration(@Param("lessonTypeId") int lessonTypeId, @Param("newDuration") Duration newDuration);

}
