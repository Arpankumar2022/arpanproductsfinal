package com.arpanbags.products.arpanbagsproducts.repository;

import com.arpanbags.products.arpanbagsproducts.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserID(Long userId);

    // Correct method for fetching based on user and product IDs
    @Query("SELECT c FROM CartItem c WHERE c.userID = :userId AND c.productID = :productId")
    List<CartItem> findByUserIdAndProductId(Long userId, Long productId);
}
