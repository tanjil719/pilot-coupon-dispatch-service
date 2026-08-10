package com.pilotcoupondispatchservice.modules.auth.repository;

import com.pilotcoupondispatchservice.modules.auth.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    Optional<OtpVerification> findByEmailAndIsVerifiedFalse(String email);

    Optional<OtpVerification> findByEmailAndOtpAndIsVerifiedFalse(String email, String otp);

    void deleteByEmail(String email);
}
