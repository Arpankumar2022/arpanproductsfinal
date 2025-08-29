package com.arpanbags.products.arpanbagsproducts.dto;

import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {

    private ProductsType product;
    private int quantity;
    private Long productID;



}
