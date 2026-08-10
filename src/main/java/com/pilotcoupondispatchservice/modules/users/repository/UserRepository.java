package com.pilotcoupondispatchservice.modules.users.repository;

import com.pilotcoupondispatchservice.modules.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Page<User> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

//    Page<User> findAllByRole(RoleLevel role, Pageable pageable);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
