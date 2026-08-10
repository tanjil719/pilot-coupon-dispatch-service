//package com.pilotcoupondispatchservice.modules.auth.controller;
//
//import com.pilotcoupondispatchservice.modules.auth.dto.*;
//import com.pilotcoupondispatchservice.modules.auth.service.AuthService;
//import com.pilotcoupondispatchservice.payloads.LoginRequest;
//import com.pilotcoupondispatchservice.payloads.LoginResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("${url.base}/public/auth/login")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
//        LoginResponse response = authService.login(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("${url.base}/public/auth/refresh")
//    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
//        TokenPairResponse response = authService.refreshToken(request.refreshToken());
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("${url.base}/public/auth/contributor/request-signup-otp")
//    public ResponseEntity<?> requestContributorSignupOtp(@Valid @RequestBody ContributorSignupRequest request) {
//        OtpResponse response = authService.requestContributorSignupOtp(request);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("${url.base}/public/auth/contributor/verify-otp-and-signup")
//    public ResponseEntity<?> verifyOtpAndSignup(@Valid @RequestBody OtpVerificationRequest request) {
//        LoginResponse response = authService.verifyOtpAndSignup(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
//}
//
//
