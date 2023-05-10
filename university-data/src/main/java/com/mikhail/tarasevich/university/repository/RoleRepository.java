package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)")
    void addRoleForUser(@Param("userId") int userId, @Param("roleId") int roleId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM user_roles WHERE user_id = :userId")
    void unbindRoleFromUser(@Param("userId") int userId);

}
