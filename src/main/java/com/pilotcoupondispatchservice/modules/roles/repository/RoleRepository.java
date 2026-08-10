package com.pilotcoupondispatchservice.modules.roles.repository;

import com.pilotcoupondispatchservice.modules.roles.entity.Role;
import com.pilotcoupondispatchservice.enums.RoleLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByAlias(String alias);

    Page<Role> findAllByAliasContainingIgnoreCase(String alias, Pageable pageable);

//    Page<Role> findAllByRoleLevel(RoleLevel roleLevel, Pageable pageable);

    boolean existsByAlias(String alias);
}
