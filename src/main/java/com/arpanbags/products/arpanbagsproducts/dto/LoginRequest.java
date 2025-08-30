package com.arpanbags.products.arpanbagsproducts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static com.arpanbags.products.arpanbagsproducts.Constants.*;
import static com.arpanbags.products.arpanbagsproducts.Constants.INVALID_PASSWORD;

@Data
public class LoginRequest {


    @NotBlank(message = MOBILE_REQUIRED)
    @Pattern(regexp = "^[0-9]{10}$", message = INVALID_MOBILE)
    private String mobileNumber;


    @NotBlank(message = PASSWORD_REQUIRED)
    @Size(min = 8, max = 64, message = INVALID_PASSWORD)
    private String password;

}
