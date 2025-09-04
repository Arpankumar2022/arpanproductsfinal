package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.dto.CategoryDTO;
import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.dto.SubCategoryDTO;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.entity.categories.Category;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.mapper.CategoryMapper;
import com.arpanbags.products.arpanbagsproducts.repository.CategoryRepository;
import com.arpanbags.products.arpanbagsproducts.repository.SubcategoryRepository;
import com.arpanbags.products.arpanbagsproducts.util.ProductTypeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    final private CategoryRepository categoryRepository;

    final private SubcategoryRepository subcategoryRepository;

    final private ProductTypeUtils productTypeUtils;


    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categoryDTOs = categoryRepository.findAll().stream().map(category -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            List<SubCategory> subCategories = subcategoryRepository.findByCategoryId(category.getId());
            List<SubCategoryDTO> subCategoryDTOs = subCategories.stream().map(sub -> {
                SubCategoryDTO subDTO = new SubCategoryDTO();
                subDTO.setId(sub.getId());
                subDTO.setName(sub.getName());
                subDTO.setProductsTypeDTOS(getAllProductsTypeFromProductTypeDTOs(sub.getProductsTypes()));
                return subDTO;
            }).collect(Collectors.toList());
            dto.setSubCategoryDTOS(subCategoryDTOs);
            return dto;
        }).collect(Collectors.toList());

        return categoryDTOs;

    }

    private List<ProductsTypeDTO> getAllProductsTypeFromProductTypeDTOs(List<ProductsType> productsTypes) {
        return productsTypes.stream()
                .map(productTypeUtils::getProductTypeDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id).map(CategoryMapper.INSTANCE::mapCategoryToCategoryDTO).orElse(null);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        return CategoryMapper.INSTANCE.mapCategoryToCategoryDTO(categoryRepository.save(CategoryMapper.INSTANCE.mapCategoryDTOToCategory(categoryDTO)));
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        return categoryRepository.findById(id).map(existing -> {
            existing.setName(updatedCategory.getName());
            return categoryRepository.save(existing);
        }).orElse(null);
    }
}