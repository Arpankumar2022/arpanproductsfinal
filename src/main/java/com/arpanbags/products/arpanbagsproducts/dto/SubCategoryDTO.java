package com.arpanbags.products.arpanbagsproducts.dto;

import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDTO {
    private Long id;
    private String name;
    private List<ProductsTypeDTO> productsTypes;
}

