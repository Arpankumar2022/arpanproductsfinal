package com.arpanbags.products.arpanbagsproducts.repository;

import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductsTypeRepository extends JpaRepository<ProductsType,Long> {

    Optional<ProductsType> findByProductName(String productName);


}
