package com.arpanbags.products.arpanbagsproducts.util;

import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.repository.ProductsTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeUtils {

    @Autowired
    private ProductsTypeRepository productsTypeRepository;

    public ProductsTypeDTO getProductTypeDTO(ProductsType productsType) {
        ProductsTypeDTO productsTypeDTO = new ProductsTypeDTO();
        productsTypeDTO.setProductLogo(productsType.getProductLogo());
        productsTypeDTO.setProductPrice(productsType.getProductPrice());
        productsTypeDTO.setProductType(productsType.getProductType());
        productsTypeDTO.setProductName(productsType.getProductName());
        productsTypeDTO.setId(productsType.getId());
        productsTypeDTO.setSubCategoryId(productsType.getSubcategoryId());
        productsTypeDTO.setFileUrls(productsTypeRepository.findImagePathsByProductTypeId(productsType.getId()));
        return productsTypeDTO;
    }


}
