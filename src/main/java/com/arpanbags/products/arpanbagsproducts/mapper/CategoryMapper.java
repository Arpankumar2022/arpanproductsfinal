package com.arpanbags.products.arpanbagsproducts.mapper;

import com.arpanbags.products.arpanbagsproducts.dto.CategoryDTO;
import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.entity.categories.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO mapCategoryToCategoryDTO(Category category);

    Category mapCategoryDTOToCategory(CategoryDTO categoryDTO);
}



