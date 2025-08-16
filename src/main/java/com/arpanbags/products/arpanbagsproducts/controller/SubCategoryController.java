package com.arpanbags.products.arpanbagsproducts.controller;

import com.arpanbags.products.arpanbagsproducts.dto.GroupedImageMetadataDTO;
import com.arpanbags.products.arpanbagsproducts.dto.SubCategoryDTO;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.service.SubCategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcategories")
@RequiredArgsConstructor
@Slf4j
public class SubCategoryController {


    private final SubCategoryService subCategoryService;

    @GetMapping
    public List<SubCategoryDTO> getAll() {
        return subCategoryService.getAllSubcategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<GroupedImageMetadataDTO>> getProductsBySubCategoryID(@PathVariable int id,
                                                                                    HttpServletRequest request) {
        log.info("Get Products By subCategoryID in SubCategoryController");
        List<GroupedImageMetadataDTO> groupedImageMetadataDTOS = subCategoryService.getImagesBySubCategoryID(id, request);
        return ResponseEntity.ok(groupedImageMetadataDTOS);
    }

    @PostMapping("/{categoryId}")
    public SubCategoryDTO create(@RequestBody SubCategoryDTO subCategoryDTO, @PathVariable Long categoryId) {
        return subCategoryService.createSubCategory(subCategoryDTO, categoryId);
    }

    @PutMapping("/{id}")
    public SubCategory update(@PathVariable Long id, @RequestBody SubCategory SubCategory) {
        return subCategoryService.updateSubCategory(id, SubCategory);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
    }
}
