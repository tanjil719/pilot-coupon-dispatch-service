package com.pilotcoupondispatchservice.modules.auth.service;

import com.pilotcoupondispatchservice.dao.EmailSenderService;
import com.pilotcoupondispatchservice.enums.UserType;
import com.pilotcoupondispatchservice.exceptions.InvalidAuthenticationException;
import com.pilotcoupondispatchservice.modules.auth.dto.ForgotPasswordRequest;
import com.pilotcoupondispatchservice.modules.auth.dto.OtpResponse;
import com.pilotcoupondispatchservice.modules.auth.dto.OtpVerificationRequest;
import com.pilotcoupondispatchservice.modules.auth.dto.OwnerSignupRequest;
import com.pilotcoupondispatchservice.modules.auth.dto.ResetPasswordRequest;
import com.pilotcoupondispatchservice.modules.auth.dto.TokenPairResponse;
import com.pilotcoupondispatchservice.modules.auth.entity.OtpVerification;
import com.pilotcoupondispatchservice.modules.auth.entity.PasswordResetOtp;
import com.pilotcoupondispatchservice.modules.auth.repository.OtpVerificationRepository;
import com.pilotcoupondispatchservice.modules.auth.repository.PasswordResetOtpRepository;
import com.pilotcoupondispatchservice.modules.roles.repository.RoleRepository;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.service.UserService;
import com.pilotcoupondispatchservice.payloads.CustomPrincipal;
import com.pilotcoupondispatchservice.payloads.LoginRequest;
import com.pilotcoupondispatchservice.payloads.LoginResponse;
import com.pilotcoupondispatchservice.utils.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final RoleRepository roleRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordResetOtpRepository passwordResetOtpRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    private static final int OTP_EXPIRY_MINUTES = 5;

    public TokenPairResponse refreshToken(String refreshToken) {
        if (!jwtUtil.isTokenValid(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new InvalidAuthenticationException("Invalid refresh token");
        }

        CustomPrincipal principal = jwtUtil.extractPrincipal(refreshToken);
        if (principal.getUserId() <= 0) {
            throw new InvalidAuthenticationException("Refresh token subject is invalid");
        }

        String accessToken = jwtUtil.generateAccessToken(principal);
        String nextRefreshToken = jwtUtil.generateRefreshToken(principal);
        return new TokenPairResponse(accessToken, nextRefreshToken, "Bearer");
    }

    public LoginResponse ownerLogin(LoginRequest request) {
        User user = userService.findByEmail(request.getUsername());

        if (user.getUserType() != UserType.OWNER) {
            throw new InvalidAuthenticationException("Invalid email or password");
        }

        return authenticate(request, user);
    }

    public LoginResponse adminLogin(LoginRequest request) {
        User user = userService.findByEmail(request.getUsername());

        if (user.getUserType() != UserType.ADMIN) {
            throw new InvalidAuthenticationException("Invalid email or password");
        }

        return authenticate(request, user);
    }

    private LoginResponse authenticate(LoginRequest request, User user) {
        if (!user.getIsActive()) {
            throw new InvalidAuthenticationException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidAuthenticationException("Invalid email or password");
        }

        CustomPrincipal principal = new CustomPrincipal(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().getPermission()
        );

        String accessToken = jwtUtil.generateAccessToken(principal);
        String refreshToken = jwtUtil.generateRefreshToken(principal);

        TokenPairResponse tokenPair = new TokenPairResponse(accessToken, refreshToken, "Bearer");
        return new LoginResponse(tokenPair);
    }

    @Transactional
    public OtpResponse requestOwnerSignup(OwnerSignupRequest request) {
        if (userService.findByEmailOptional(request.getEmail()).isPresent()) {
            throw new InvalidAuthenticationException("Email already registered");
        }

        // Delete any existing unverified OTP for this email
        otpVerificationRepository.deleteByEmail(request.getEmail());

        // Generate OTP
        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(request.getEmail());
        otpVerification.setOtp(otp);
        otpVerification.setExpiryTime(expiryTime);
        otpVerification.setIsVerified(false);
        otpVerification.setName(request.getName());
        otpVerification.setPhone(request.getPhone());
        otpVerification.setPassword(passwordEncoder.encode(request.getPassword()));

        otpVerificationRepository.save(otpVerification);

        // Send OTP via email
        sendOtpEmail(request.getEmail(), otp);

        return new OtpResponse("OTP sent to email", request.getEmail(), "PENDING");
    }

    @Transactional
    public LoginResponse verifyOtpAndSignup(OtpVerificationRequest request) {
        OtpVerification otpVerification = otpVerificationRepository
                .findByEmailAndOtpAndIsVerifiedFalse(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new InvalidAuthenticationException("Invalid OTP or email"));

        if (LocalDateTime.now().isAfter(otpVerification.getExpiryTime())) {
            otpVerificationRepository.delete(otpVerification);
            throw new InvalidAuthenticationException("OTP has expired");
        }

        // Create user
        User user = new User();
        user.setName(otpVerification.getName());
        user.setEmail(otpVerification.getEmail());
        user.setUserType(UserType.OWNER);
        user.setPhone(otpVerification.getPhone());
        user.setPassword(otpVerification.getPassword());
        user.setRole(roleRepository.findByAlias("OWNER").orElseThrow(() -> new RuntimeException("Role not found")));
        user.setIsActive(true);

        User savedUser = userService.createUserDirect(user);

        // Mark OTP as verified
        otpVerification.setIsVerified(true);
        otpVerificationRepository.save(otpVerification);

        // Generate JWT tokens
        CustomPrincipal principal = new CustomPrincipal(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName(),
                savedUser.getRole().getPermission()
        );

        String accessToken = jwtUtil.generateAccessToken(principal);
        String refreshToken = jwtUtil.generateRefreshToken(principal);

        TokenPairResponse tokenPair = new TokenPairResponse(accessToken, refreshToken, "Bearer");
        return new LoginResponse(tokenPair);
    }

    @Transactional
    public OtpResponse requestPasswordReset(ForgotPasswordRequest request) {
        userService.findByEmail(request.getEmail());

        passwordResetOtpRepository.deleteByEmail(request.getEmail());

        String otp = generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        PasswordResetOtp passwordResetOtp = new PasswordResetOtp();
        passwordResetOtp.setEmail(request.getEmail());
        passwordResetOtp.setOtp(otp);
        passwordResetOtp.setExpiryTime(expiryTime);
        passwordResetOtp.setIsVerified(false);

        passwordResetOtpRepository.save(passwordResetOtp);

        sendPasswordResetOtpEmail(request.getEmail(), otp);

        return new OtpResponse("OTP sent to email", request.getEmail(), "PENDING");
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetOtp passwordResetOtp = passwordResetOtpRepository
                .findByEmailAndOtpAndIsVerifiedFalse(request.getEmail(), request.getOtp())
                .orElseThrow(() -> new InvalidAuthenticationException("Invalid OTP or email"));

        if (LocalDateTime.now().isAfter(passwordResetOtp.getExpiryTime())) {
            passwordResetOtpRepository.delete(passwordResetOtp);
            throw new InvalidAuthenticationException("OTP has expired");
        }

        userService.resetPasswordByEmail(request.getEmail(), request.getNewPassword());

        passwordResetOtp.setIsVerified(true);
        passwordResetOtpRepository.save(passwordResetOtp);
    }

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Owner Signup OTP");
        message.setText("Your OTP for Owner signup is: " + otp + "\n\n" +
                "This OTP will expire in " + OTP_EXPIRY_MINUTES + " minutes.\n\n" +
                "If you didn't request this, please ignore this email.");

        emailSenderService.sendEmailNotificationAsync(message);
    }

    private void sendPasswordResetOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Password Reset OTP");
        message.setText("Your OTP for password reset is: " + otp + "\n\n" +
                "This OTP will expire in " + OTP_EXPIRY_MINUTES + " minutes.\n\n" +
                "If you didn't request this, please ignore this email.");

        emailSenderService.sendEmailNotificationAsync(message);
    }
}



