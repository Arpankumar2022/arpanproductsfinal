package com.arpanbags.products.arpanbagsproducts.mapper;


import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductsTypeMapper {
    ProductsTypeMapper INSTANCE = Mappers.getMapper(ProductsTypeMapper.class);

    ProductsTypeDTO mapProductsTypeToProductsTypeDTO(ProductsType productsType);

    ProductsType mapProductsTypeDTOToProductsType(ProductsTypeDTO productsTypeDto);


}
