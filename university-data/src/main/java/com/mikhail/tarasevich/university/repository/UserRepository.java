package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Gender;
import com.mikhail.tarasevich.university.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@NoRepositoryBean
public interface UserRepository <E extends User> extends JpaRepository<E, Integer> {

    Optional<E> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.gender = :gender, u.email = :email " +
            "WHERE u.id = :id")
    void updateGeneralInfo(@Param("id") int id, @Param("firstName") String firstName,
                           @Param("lastName") String lastName, @Param("gender") Gender gender,
                           @Param("email") String email);

    void updateUserPassword(int id, String password);

}
