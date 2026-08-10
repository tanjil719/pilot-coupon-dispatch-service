package com.pilotcoupondispatchservice.modules.users.repository;

import com.pilotcoupondispatchservice.modules.users.entity.Contributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContributorRepository extends JpaRepository<Contributor, Long> {

    Optional<Contributor> findByUserId(Long userId);

    Page<Contributor> findAllByIsApprovedTrue(Pageable pageable);

    Page<Contributor> findAllByIsBannedTrue(Pageable pageable);

    long countByIsApprovedTrue();

    long countByIsApprovedFalse();

    long countByIsBannedTrue();
}
