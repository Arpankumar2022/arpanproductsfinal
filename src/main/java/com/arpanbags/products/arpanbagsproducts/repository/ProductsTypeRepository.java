package com.arpanbags.products.arpanbagsproducts.repository;

import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductsTypeRepository extends JpaRepository<ProductsType,Long> {

    Optional<ProductsType> findByProductName(String productName);

    @Query(value = "SELECT i.image_paths FROM products_type_image_paths i WHERE i.products_type_id = :productTypeId", nativeQuery = true)
    List<String> findImagePathsByProductTypeId(@Param("productTypeId") Long productTypeId);

}
