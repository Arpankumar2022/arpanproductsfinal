package com.arpanbags.products.arpanbagsproducts.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ResetPasswordRequest {
    private String mobileNumber;
    private String newPassword;

    // Getter and Setter
}
