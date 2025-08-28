package com.arpanbags.products.arpanbagsproducts.repository;


import com.arpanbags.products.arpanbagsproducts.entity.categories.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<SubCategory, Long> {

    List<SubCategory> findByCategoryId(Long categoryId);
}
