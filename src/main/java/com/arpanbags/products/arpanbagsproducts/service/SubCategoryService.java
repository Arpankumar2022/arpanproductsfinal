package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.dto.SubCategoryDTO;
import com.arpanbags.products.arpanbagsproducts.entity.categories.Category;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.mapper.SubCategoryMapper;
import com.arpanbags.products.arpanbagsproducts.repository.CategoryRepository;
import com.arpanbags.products.arpanbagsproducts.repository.SubcategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService {


    private final SubcategoryRepository subCategoryRepository;


    private final CategoryRepository categoryRepository;

    public List<SubCategoryDTO> getAllSubcategories() {
        return subCategoryRepository.findAll().stream().map(SubCategoryMapper.INSTANCE::mapSubCategoryToSubCategoryDTO)
                .collect(Collectors.toList());
    }

    public SubCategoryDTO getSubCategory(Long id) {
        return subCategoryRepository.findById(id)
                .map(SubCategoryMapper.INSTANCE::mapSubCategoryToSubCategoryDTO)
                .orElse(null);

    }

    public SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            SubCategory subCategory = new SubCategory();
            subCategory.setCategoryId(categoryId);
            subCategory.setName(subCategoryDTO.getName());
            //subCategory.setProductsTypes();
           // subCategory.set
            return SubCategoryMapper.INSTANCE.mapSubCategoryToSubCategoryDTO(subCategoryRepository.save(subCategory));

          //  return SubCategoryMapper.INSTANCE.mapSubCategoryToSubCategoryDTO(subCategoryRepository.save(SubCategoryMapper.INSTANCE.mapSubCategoryDTOToSubCategory(subCategoryDTO)));
        }
        return null;
    }

    public void deleteSubCategory(Long id) {
        subCategoryRepository.deleteById(id);
    }

    public SubCategory updateSubCategory(Long id, SubCategory updated) {
        return null;/*subCategoryRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    return subCategoryRepository.save(existing);
                })
                .orElse(null);*/
    }
}
