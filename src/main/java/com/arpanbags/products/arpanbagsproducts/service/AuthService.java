package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.dto.*;
import com.arpanbags.products.arpanbagsproducts.entity.Role;
import com.arpanbags.products.arpanbagsproducts.entity.User;
import com.arpanbags.products.arpanbagsproducts.entity.UserOtp;
import com.arpanbags.products.arpanbagsproducts.repository.UserOtpRepository;
import com.arpanbags.products.arpanbagsproducts.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service

public class AuthService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final Msg91OtpService msg91OtpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, UserOtpRepository userOtpRepository, Msg91OtpService msg91OtpService, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
        this.msg91OtpService = msg91OtpService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public void register(RegisterRequest request) {
        if (userRepository.findByMobileNumber(request.getMobileNumber()).isPresent()) {
            throw new RuntimeException("Mobile number already registered");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setCompanyName(request.getCompanyName());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hashing here
        user.setAddress(request.getAddress());
        user.setStatus(User.Status.INACTIVE);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        sendOtp(request.getMobileNumber());
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("User is not active");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(toList());

        String token = jwtService.generateToken(user.getMobileNumber(), roles);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setFullName(user.getCompanyName());
        response.setRoles(roles);
        return response;
    }

    public void sendOtp(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("User is not active");
        }

        String otp = String.format("%06d", new SecureRandom().nextInt(999999));

        UserOtp userOtp = new UserOtp();
        userOtp.setUser(user);
        userOtp.setOtp(otp);
        userOtp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        userOtpRepository.save(userOtp);

        msg91OtpService.sendOtp(mobileNumber, otp);
    }

    public String verifyOtp(OtpVerifyRequest request) {
        User user = userRepository.findByMobileNumber(request.getMobileNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserOtp userOtp = userOtpRepository
                .findTopByUserAndOtpAndIsUsedFalseOrderByCreatedAtDesc(user, request.getOtp())
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (userOtp.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        userOtp.setUsed(true);
        userOtpRepository.save(userOtp);

        if (user.getStatus() == User.Status.INACTIVE) {
            user.setStatus(User.Status.ACTIVE);
            userRepository.save(user);
        }

        return "OTP verified. Login successful.";
    }

    public void resetPassword(String mobileNumber, String newPassword) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("User is not active");
        }

        // Hash the new password
        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);

        userRepository.save(user);

        // Optional: Send confirmation SMS
        msg91OtpService.sendPasswordResetConfirmation(user.getMobileNumber(), user.getCompanyName());
    }

}
