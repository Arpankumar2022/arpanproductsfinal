package com.arpanbags.products.arpanbagsproducts.mapper;

import com.arpanbags.products.arpanbagsproducts.dto.CategoryDTO;
import com.arpanbags.products.arpanbagsproducts.dto.SubCategoryDTO;
import com.arpanbags.products.arpanbagsproducts.entity.categories.Category;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubCategoryMapper {
    SubCategoryMapper INSTANCE = Mappers.getMapper(SubCategoryMapper.class);

    SubCategoryDTO mapSubCategoryToSubCategoryDTO(SubCategory subCategory);

    SubCategory mapSubCategoryDTOToSubCategory(SubCategoryDTO subCategoryDTO);
}
