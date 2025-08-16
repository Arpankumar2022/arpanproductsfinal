package com.arpanbags.products.arpanbagsproducts.repository;

import com.arpanbags.products.arpanbagsproducts.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
