package com.arpanbags.products.arpanbagsproducts.dto;


import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {

    private Long id;
    private String name;
    private String address;
    private String city;
    private String restaurantDescription;
}
