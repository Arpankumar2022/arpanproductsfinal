package com.arpanbags.products.arpanbagsproducts.service;

import com.arpanbags.products.arpanbagsproducts.dto.CartItemDTO;
import com.arpanbags.products.arpanbagsproducts.dto.ProductsTypeDTO;
import com.arpanbags.products.arpanbagsproducts.entity.CartItem;
import com.arpanbags.products.arpanbagsproducts.entity.ProductsType;
import com.arpanbags.products.arpanbagsproducts.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemService {


    private final CartItemRepository cartItemRepository;

    private final ProductTypeService productTypeService; // To get ProductsTypeDTO by productID

    public CartItemDTO createCartItem(CartItemDTO cartItemDTO, Long userId) {

        CartItem cartItem = new CartItem();
        cartItem.setUserID(userId);
        cartItem.setProductID(cartItemDTO.getProduct().getId()); // assuming ProductsTypeDTO has getId()
        cartItem.setQuantity(cartItemDTO.getQuantity());
        CartItem saved = cartItemRepository.save(cartItem);
        return mapToDTO(saved);
    }

    public List<CartItemDTO> getCartItemsByUser(Long userId) {
        List<CartItem> cartItems = cartItemRepository.findByUserID(userId);
        return cartItems.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CartItemDTO getCartItemById(Long id) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
        return mapToDTO(item);
    }

    public CartItemDTO updateCartItem(Long id, CartItemDTO dto) {
        CartItem existing = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        existing.setProductID(dto.getProduct().getId());
        CartItem updated = cartItemRepository.save(existing);

        return mapToDTO(updated);
    }

    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    private CartItemDTO mapToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(cartItem.getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setProduct(productTypeService.getProductTypeById(cartItem.getProductID()));
        return dto;
    }

    public void deleteItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    public boolean getExistingCartItems(Long userId, Long productId) {
        List<CartItem> existingCart = cartItemRepository.findByUserIdAndProductId(userId, productId);
        return !existingCart.isEmpty();
    }
}
