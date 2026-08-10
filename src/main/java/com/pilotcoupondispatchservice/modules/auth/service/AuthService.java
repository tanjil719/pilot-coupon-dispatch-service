package com.pilotcoupondispatchservice.modules.auth.service;

import com.pilotcoupondispatchservice.exceptions.InvalidAuthenticationException;
import com.pilotcoupondispatchservice.modules.auth.dto.TokenPairResponse;
import com.pilotcoupondispatchservice.modules.auth.repository.OtpVerificationRepository;
import com.pilotcoupondispatchservice.modules.users.entity.User;
import com.pilotcoupondispatchservice.modules.users.service.UserService;
import com.pilotcoupondispatchservice.payloads.CustomPrincipal;
import com.pilotcoupondispatchservice.payloads.LoginRequest;
import com.pilotcoupondispatchservice.payloads.LoginResponse;
import com.pilotcoupondispatchservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    //    private final EmailSenderService emailSenderService;
    private final OtpVerificationRepository otpVerificationRepository;

//    @Value("${spring.mail.username}")
//    private String fromEmail;

    private static final int OTP_EXPIRY_MINUTES = 10;

//    public TokenPairResponse refreshToken(String refreshToken) {
//        if (!jwtUtil.isTokenValid(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
//            throw new InvalidAuthenticationException("Invalid refresh token");
//        }
//
//        CustomPrincipal principal = jwtUtil.extractPrincipal(refreshToken);
//        if (principal.userId() == null || principal.userId().isBlank()) {
//            throw new InvalidAuthenticationException("Refresh token subject is invalid");
//        }
//
//        String accessToken = jwtUtil.generateAccessToken(principal);
//        String nextRefreshToken = jwtUtil.generateRefreshToken(principal);
//        return new TokenPairResponse(accessToken, nextRefreshToken, "Bearer");
//    }

    public LoginResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getUsername());

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
        return new LoginResponse(tokenPair, user);
    }

//    @Transactional
//    public OtpResponse requestContributorSignupOtp(ContributorSignupRequest request) {
//        if (userService.findByEmailOptional(request.getEmail()).isPresent()) {
//            throw new InvalidAuthenticationException("Email already registered");
//        }
//
//        // Delete any existing unverified OTP for this email
//        otpVerificationRepository.deleteByEmail(request.getEmail());
//
//        // Generate OTP
//        String otp = generateOtp();
//        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);
//
//        OtpVerification otpVerification = new OtpVerification();
//        otpVerification.setEmail(request.getEmail());
//        otpVerification.setOtp(otp);
//        otpVerification.setExpiryTime(expiryTime);
//        otpVerification.setIsVerified(false);
//        otpVerification.setName(request.getName());
//        otpVerification.setPhone(request.getPhone());
//        otpVerification.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        otpVerificationRepository.save(otpVerification);
//
//        // Send OTP via email
////        sendOtpEmail(request.getEmail(), otp);
//
//        return new OtpResponse("OTP sent to email", request.getEmail(), "PENDING");
//    }
//
//    @Transactional
//    public LoginResponse verifyOtpAndSignup(OtpVerificationRequest request) {
//        OtpVerification otpVerification = otpVerificationRepository
//                .findByEmailAndOtpAndIsVerifiedFalse(request.getEmail(), request.getOtp())
//                .orElseThrow(() -> new InvalidAuthenticationException("Invalid OTP or email"));
//
//        if (LocalDateTime.now().isAfter(otpVerification.getExpiryTime())) {
//            otpVerificationRepository.delete(otpVerification);
//            throw new InvalidAuthenticationException("OTP has expired");
//        }
//
//        // Create user
//        User user = new User();
//        user.setName(otpVerification.getName());
//        user.setEmail(otpVerification.getEmail());
//        user.setPhone(otpVerification.getPhone());
//        user.setPassword(otpVerification.getPassword());
//        user.setRole(com.pilotcoupondispatchservice.enums.RoleLevel.CONTRIBUTOR);
//        user.setIsActive(true);
//
//        User savedUser = userService.createUserDirect(user);
//
//        // Create contributor profile
//        Contributor contributor = new Contributor();
//        contributor.setUser(savedUser);
//        contributor.setIsApproved(false);
//        contributor.setIsBanned(false);
//
//        userService.createContributorDirect(contributor);
//
//        // Mark OTP as verified
//        otpVerification.setIsVerified(true);
//        otpVerificationRepository.save(otpVerification);
//
//        // Generate JWT tokens
//        CustomPrincipal principal = new CustomPrincipal(
//                savedUser.getId().toString(),
//                savedUser.getEmail(),
//                savedUser.getName(),
//                savedUser.getRole().name()
//        );
//
//        String accessToken = jwtUtil.generateAccessToken(principal);
//        String refreshToken = jwtUtil.generateRefreshToken(principal);
//
//        TokenPairResponse tokenPair = new TokenPairResponse(accessToken, refreshToken, "Bearer");
//        return new LoginResponse(tokenPair, savedUser);
//    }
//
//    private String generateOtp() {
//        return String.format("%06d", new Random().nextInt(999999));
//    }

//    private void sendOtpEmail(String email, String otp) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(fromEmail);
//        message.setTo(email);
//        message.setSubject("Train Tracker - Contributor Signup OTP");
//        message.setText("Your OTP for contributor signup is: " + otp + "\n\n" +
//                "This OTP will expire in " + OTP_EXPIRY_MINUTES + " minutes.\n\n" +
//                "If you didn't request this, please ignore this email.");
//
//        emailSenderService.sendEmailNotificationAsync(message);
//    }
}



