package com.arpanbags.products.arpanbagsproducts.repository;

import com.arpanbags.products.arpanbagsproducts.entity.User;
import com.arpanbags.products.arpanbagsproducts.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOtpRepository extends JpaRepository<UserOtp, Long> {
    Optional<UserOtp> findTopByUserAndOtpAndIsUsedFalseOrderByCreatedAtDesc(User user, String otp);
}
