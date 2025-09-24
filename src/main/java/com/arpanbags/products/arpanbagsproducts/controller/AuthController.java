package com.arpanbags.products.arpanbagsproducts.controller;

import com.arpanbags.products.arpanbagsproducts.dto.*;
import com.arpanbags.products.arpanbagsproducts.service.AuthService;
import com.arpanbags.products.arpanbagsproducts.service.ProfaneWordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.arpanbags.products.arpanbagsproducts.Constants.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ProfaneWordService profaneWordService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request, BindingResult result) {

        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            RegisterResponse responseValidationErrors = new RegisterResponse(false, "Validation error(s): " + errorMessage);
            return ResponseEntity.badRequest().body(responseValidationErrors);
        }

        Map<String, String> fieldsToValidate = Map.of(
                MOBILE_NUMBER, request.getMobileNumber(),
                COMPANY_NAME, request.getCompanyName(),
                EMAIL, request.getEmail(),
                ADDRESS, request.getAddress(),
                PASSWORD, request.getPassword()
        );

        Optional<String> invalidField = profaneWordService.findInvalidField(fieldsToValidate);
        if (invalidField.isPresent()) {
            RegisterResponse responseForProfaneWords = new RegisterResponse(false, FIELD + invalidField.get() + CONTAINS_VULGAR_JUNKS);
            return ResponseEntity.badRequest().body(responseForProfaneWords);
        }

        RegisterResponse response = authService.register(request);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else if (response.getMessage().equals(MOBILE_NUMBER_ALREADY_REGISTERED)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.getMessage().equals(EMAIL_ALREADY_REGISTERED)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            LoginResponse loginValidationErrors = new LoginResponse(null, null, Collections.emptyList(), "Validation error(s): " + errorMessage);
            return ResponseEntity.badRequest().body(loginValidationErrors);
        }
        LoginResponse loginResponse = authService.login(request);
        if (loginResponse.getError() != null) {
            return ResponseEntity.badRequest().body(loginResponse);
        } else {
            return ResponseEntity.ok().body(loginResponse);
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String mobileNumber) {
        authService.sendOtp(mobileNumber);
        return ResponseEntity.ok("OTP sent.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest request) {
        String result = authService.verifyOtp(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getMobileNumber(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successfully.");
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

}
