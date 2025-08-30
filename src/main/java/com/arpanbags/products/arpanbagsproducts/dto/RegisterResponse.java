package com.arpanbags.products.arpanbagsproducts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RegisterResponse {
    private boolean success;
    private String message;
}