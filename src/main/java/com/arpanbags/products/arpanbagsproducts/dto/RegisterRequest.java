package com.arpanbags.products.arpanbagsproducts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

import static com.arpanbags.products.arpanbagsproducts.Constants.*;

@Setter
@Getter
@Data
public class RegisterRequest {

    @NotBlank(message = EMAIL_REQUIRED)
    @Email(message = INVALID_EMAIL)
    private String email;

    @NotBlank(message = MOBILE_REQUIRED)
    @Pattern(regexp = "^[0-9]{10}$", message = INVALID_MOBILE)
    private String mobileNumber;

    @NotBlank(message = COMPANY_REQUIRED)
    @Size(min = 2, max = 100, message = INVALID_COMPANY)
    private String companyName;

    @NotBlank(message = PASSWORD_REQUIRED)
    @Size(min = 8, max = 64, message = INVALID_PASSWORD)
    private String password;

    @NotBlank(message = ADDRESS_REQUIRED)
    @Size(min = 5, max = 200, message = INVALID_ADDRESS)
    private String address;
}
