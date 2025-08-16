package com.arpanbags.products.arpanbagsproducts.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class RegisterRequest {
    private String email;
    private String mobileNumber;
    private String companyName;
    private String password;
    private String address;
}
