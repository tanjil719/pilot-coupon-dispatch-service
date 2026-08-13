package com.pilotcoupondispatchservice.modules.auth.repository;

import com.pilotcoupondispatchservice.modules.auth.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, Long> {

    Optional<PasswordResetOtp> findByEmailAndOtpAndIsVerifiedFalse(String email, String otp);

    void deleteByEmail(String email);
}
