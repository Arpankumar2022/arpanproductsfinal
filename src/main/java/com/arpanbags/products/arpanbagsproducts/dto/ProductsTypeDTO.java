package com.arpanbags.products.arpanbagsproducts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsTypeDTO {
    private Long id;
    private String productType;
    private String productName;

    private String productLogo;

    private String offer;

    private Double productPrice;

    private List<MultipartFile> images;

    private Long subCategoryId;

}
