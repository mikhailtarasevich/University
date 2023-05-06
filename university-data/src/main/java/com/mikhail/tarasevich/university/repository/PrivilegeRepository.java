package com.mikhail.tarasevich.university.repository;

import com.mikhail.tarasevich.university.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    Optional<Privilege> findByName(String name);

    @Query("SELECT p FROM Privilege p LEFT JOIN p.roles r WHERE r.id = :roleId")
    List<Privilege> findPrivilegesRelateToRole(@Param("roleId") int roleId);

    @Query("SELECT p FROM Privilege p LEFT JOIN p.roles r LEFT JOIN r.users u WHERE u.email = :email")
    List<Privilege> findPrivilegesRelateToUser(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "INSERT INTO role_privileges (role_id, privilege_id) VALUES (:roleId, :privilegeId)")
    void addPrivilegeToRole(@Param("roleId") int roleId, @Param("privilegeId") int privilegeId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Privilege WHERE id IN (SELECT r.id FROM Role r JOIN r.privileges p WHERE p.id = :roleId)")
    void unbindPrivilegeFromRole(@Param("roleId") int roleId);

}
