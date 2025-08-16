package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.entity.CartItem;
import com.arpanbags.products.arpanbagsproducts.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;

    public List<CartItem> getAllItems() {
        return cartItemRepository.findAll();
    }

    public CartItem addItem(CartItem item) {
        return cartItemRepository.save(item);
    }

    public CartItem updateItem(Long id, CartItem itemDetails) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        item.setProductName(itemDetails.getProductName());
        item.setQuantity(itemDetails.getQuantity());
        item.setPrice(itemDetails.getPrice());
        return cartItemRepository.save(item);
    }

    public void deleteItem(Long id) {
        cartItemRepository.deleteById(id);
    }
}
