package com.arpanbags.products.arpanbagsproducts.controller;

import com.arpanbags.products.arpanbagsproducts.entity.CartItem;
import com.arpanbags.products.arpanbagsproducts.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {


    private final CartService cartService;

    @GetMapping
    public List<CartItem> getAllItems() {
        return cartService.getAllItems();
    }

    @PostMapping
    public ResponseEntity<CartItem> addItem(@RequestBody CartItem item) {
        return new ResponseEntity<>(cartService.addItem(item), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateItem(@PathVariable Long id, @RequestBody CartItem itemDetails) {
        return new ResponseEntity<>(cartService.updateItem(id, itemDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        cartService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
