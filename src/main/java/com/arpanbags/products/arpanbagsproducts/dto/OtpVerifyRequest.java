package com.arpanbags.products.arpanbagsproducts.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OtpVerifyRequest {
    private String mobileNumber;
    private String otp;
}
