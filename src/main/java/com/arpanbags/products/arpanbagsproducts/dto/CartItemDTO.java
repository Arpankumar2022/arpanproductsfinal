package com.arpanbags.products.arpanbagsproducts.dto;

import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import lombok.Data;


@Data
public class CartItemDTO {

    private Long id;
    private int quantity;
    private ProductsTypeDTO product;
}
