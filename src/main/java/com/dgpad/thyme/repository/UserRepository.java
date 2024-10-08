package com.dgpad.thyme.repository;

import com.dgpad.thyme.model.User;
import com.dgpad.thyme.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(value = "select * from user where username = ?1", nativeQuery = true)
    Optional<User> findUserByUserName(String username);
    boolean existsByUsername(String username);
    List<User> findAllByRole(Role role);

    @Query(value = "select * from user where email = ?1", nativeQuery = true)
    Optional<User> findUserByEmail(String email);
    @Query(value = "SELECT * FROM user WHERE deleted = 0 ORDER BY enabled DESC", nativeQuery = true)
    List<User> findAllNotBlocked();

    @Query(value = "select * from user where email = ?1 and username = ?2", nativeQuery = true)
    Optional<User> findUserByEmailAndName(String email,String username);

    boolean existsByEmail(String email);

}