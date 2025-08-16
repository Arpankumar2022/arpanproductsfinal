package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.entity.User;
import com.arpanbags.products.arpanbagsproducts.entity.UserOtp;
import com.arpanbags.products.arpanbagsproducts.repository.UserOtpRepository;
import com.arpanbags.products.arpanbagsproducts.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final Msg91OtpService msg91OtpService;

    public OtpService(UserRepository userRepository, UserOtpRepository userOtpRepository, Msg91OtpService msg91OtpService) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
        this.msg91OtpService = msg91OtpService;
    }

    public void sendOtp(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != User.Status.ACTIVE) {
            throw new RuntimeException("User is not active");
        }
        String otp = String.format("%06d", new Random().nextInt(999999));

        UserOtp otpEntity = new UserOtp();
        otpEntity.setUser(user);
        otpEntity.setOtp(otp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        userOtpRepository.save(otpEntity);

        msg91OtpService.sendOtp(mobileNumber, otp);
    }
}
