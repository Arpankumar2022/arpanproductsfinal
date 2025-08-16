package com.arpanbags.products.arpanbagsproducts.controller;

import com.arpanbags.products.arpanbagsproducts.dto.SubCategoryDTO;
import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import com.arpanbags.products.arpanbagsproducts.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcategories")
@RequiredArgsConstructor
public class SubCategoryController {


    private final SubCategoryService subCategoryService;

    @GetMapping
    public List<SubCategoryDTO> getAll() {
        return subCategoryService.getAllSubcategories();
    }

    @GetMapping("/{id}")
    public SubCategoryDTO get(@PathVariable Long id) {
        return subCategoryService.getSubCategory(id);
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
