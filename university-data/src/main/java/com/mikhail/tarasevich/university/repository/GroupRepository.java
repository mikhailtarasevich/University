package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    Optional<Group> findByName(String name);

    List<Group> findGroupByTeachersId(int teacherId);

    @Query("SELECT g FROM Group g WHERE g.id <> ALL (SELECT g.id FROM Group g LEFT JOIN g.teachers t WHERE t.id = :teacherId)")
    List<Group> findGroupsNotRelateToTeacher(@Param("teacherId") int teacherId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.name = :name, g.faculty.id = :facultyId, g.headStudent.id = :studentId, " +
            "g.educationForm.id = :edFormId WHERE g.id = :id")
    void update(@Param("id") int id, @Param("name") String name, @Param("facultyId") Integer facultyId,
                @Param("studentId") Integer studentId, @Param("edFormId") Integer edFormId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.faculty.id = :newFacultyId WHERE g.id = :groupId")
    void changeFaculty(@Param("groupId") int groupId, @Param("newFacultyId") int newFacultyId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.headStudent.id = :newUserId WHERE g.id = :groupId")
    void changeHeadUser(@Param("groupId") int groupId, @Param("newUserId") int newUserId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.educationForm.id = :newEducationFormId WHERE g.id = :groupId")
    void changeEducationForm(@Param("groupId") int groupId, @Param("newEducationFormId") int newEducationFormId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.headStudent.id = NULL WHERE g.headStudent.id = :studentId")
    void unbindGroupsFromStudent(@Param("studentId") int studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Group WHERE id IN (SELECT g.id FROM Group g JOIN g.teachers t WHERE t.id = :teacherId)")
    void unbindGroupsFromTeacher(@Param("teacherId") int teacherId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.educationForm.id = NULL WHERE g.educationForm.id = :educationFormId")
    void unbindGroupsFromEducationForm(@Param("educationFormId") int educationFormId);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.faculty.id = NULL WHERE g.faculty.id = :facultyId")
    void unbindGroupsFromFaculty(@Param("facultyId") int facultyId);

}
