package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.controller.ImageController;
import com.arpanbags.products.arpanbagsproducts.dto.GroupedImageMetadataDTO;
import com.arpanbags.products.arpanbagsproducts.dto.SubCategoryDTO;
import com.arpanbags.products.arpanbagsproducts.entity.categories.Category;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.mapper.SubCategoryMapper;
import com.arpanbags.products.arpanbagsproducts.repository.CategoryRepository;
import com.arpanbags.products.arpanbagsproducts.repository.SubcategoryRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubCategoryService {

    private static final String IMAGE_UPLOAD_DIR = "/opt/arpanbags/uploads/images/";
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

    public List<GroupedImageMetadataDTO> getImagesBySubCategoryID(int subCategoryID, HttpServletRequest request
    ) {

        String baseUrl = ImageController.getBaseUrl(request);
        String categoryIdStr = String.valueOf(subCategoryID);
        //PROD_ENV--->
        File categoryFolder = new File(IMAGE_UPLOAD_DIR, categoryIdStr);
       // File categoryFolder = new File("C:\\Users\\arpan\\uploads\\", categoryIdStr);

        if (!categoryFolder.exists() || !categoryFolder.isDirectory()) {
            return Collections.emptyList();
        }

        File[] productFolders = Optional.ofNullable(categoryFolder.listFiles(File::isDirectory))
                .orElse(new File[0]);

        // Map to hold grouped results
        Map<String, List<String>> groupedFiles = new HashMap<>();

        for (File productFolder : productFolders) {
            String productName = productFolder.getName();

            File[] imageFiles = Optional.ofNullable(productFolder.listFiles(File::isFile))
                    .orElse(new File[0]);

            for (File file : imageFiles) {
                String fileName = file.getName();
                String productType = parseProductType(fileName);
                if (productType == null) continue;

                String groupKey = productName + "|" + productType;

                String fileUrl = baseUrl + "/images/files/" + subCategoryID + "/" + productName + "/" + fileName;
                groupedFiles.computeIfAbsent(groupKey, k -> new ArrayList<>()).add(fileUrl);
            }
        }

        // Convert map to list of GroupedImageMetadata
        List<GroupedImageMetadataDTO> result = groupedFiles.entrySet().stream()
                .map(entry -> {
                    String[] keys = entry.getKey().split("\\|");
                    return new GroupedImageMetadataDTO(
                            entry.getValue(),
                            keys[1],         // productType
                            keys[0],         // productName
                            subCategoryID
                    );
                })
                .collect(Collectors.toList());

        return result;
    }

    private String parseProductType(String fileName) {
        String[] parts = fileName.split("_");
        return parts.length >= 3 ? parts[1] : null;
    }

}
