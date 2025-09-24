package com.arpanbags.products.arpanbagsproducts.controller;

import com.arpanbags.products.arpanbagsproducts.dto.CartItemDTO;
import com.arpanbags.products.arpanbagsproducts.service.CartItemService;
import com.arpanbags.products.arpanbagsproducts.service.impl.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity<?> addCartItem(@RequestBody CartItemDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (cartItemService.getExistingCartItems(userDetails.getUserId(), dto.getProduct().getId())) {
                return ResponseEntity.badRequest().body("Cart item already exists for this user and product.");
            }
            CartItemDTO cartItemDTO = cartItemService.createCartItem(dto, userDetails.getUserId());
            return ResponseEntity.ok(cartItemDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getCartItemsByUser")
    public ResponseEntity<List<CartItemDTO>> getCartItemsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return ResponseEntity.ok(cartItemService.getCartItemsByUser(userDetails.getUserId()));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable Long id) {
        return ResponseEntity.ok(cartItemService.getCartItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemDTO> updateCartItem(@PathVariable Long id, @RequestBody CartItemDTO dto) {
        return ResponseEntity.ok(cartItemService.updateCartItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
